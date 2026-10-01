import React, { useState, useRef, useEffect } from 'react';
import { chatAPI } from '../services/api';
import { Sparkles, Send, X, Bot } from 'lucide-react';

const AiChatWidget = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [input, setInput] = useState('');
  const [conversationId, setConversationId] = useState(null);
  const [messages, setMessages] = useState([
    {
      sender: 'ai',
      text: 'Namaste! Welcome to StayNjoy. I am your AI Travel Assistant. Looking for luxury resort recommendations or booking assistance?',
    },
  ]);
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    if (isOpen) scrollToBottom();
  }, [messages, isOpen]);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim() || loading) return;

    const userText = input.trim();
    setInput('');
    setMessages((prev) => [...prev, { sender: 'user', text: userText }]);
    setLoading(true);

    try {
      const res = await chatAPI.sendMessage(userText, conversationId);
      const { reply, conversationId: newConvId } = res.data;
      setConversationId(newConvId);
      setMessages((prev) => [...prev, { sender: 'ai', text: reply }]);
    } catch (err) {
      setMessages((prev) => [
        ...prev,
        { sender: 'ai', text: "Sorry, I couldn't connect right now. Please try again shortly!" },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <button 
        className="ai-widget-button" 
        onClick={() => setIsOpen(!isOpen)} 
        title="AI Travel Assistant"
      >
        <Sparkles className="w-6 h-6" />
      </button>

      {isOpen && (
        <div className="chat-modal">
          <div className="chat-header">
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <div style={{ padding: '6px', background: 'rgba(6, 182, 212, 0.2)', borderRadius: '8px' }}>
                <Bot className="w-5 h-5" style={{ color: 'var(--cyan-accent)' }} />
              </div>
              <div>
                <h4 style={{ fontSize: '0.95rem', fontWeight: 700, color: '#FFFFFF' }}>StayNjoy AI Concierge</h4>
                <p style={{ fontSize: '0.75rem', color: '#94A3B8' }}>Instant Travel Assistance</p>
              </div>
            </div>
            <button onClick={() => setIsOpen(false)} style={{ background: 'none', border: 'none', color: '#94A3B8', cursor: 'pointer' }}>
              <X className="w-5 h-5" />
            </button>
          </div>

          <div className="chat-messages">
            {messages.map((msg, index) => (
              <div 
                key={index} 
                className={`chat-bubble ${msg.sender === 'user' ? 'chat-bubble-user' : 'chat-bubble-ai'}`}
              >
                {msg.text}
              </div>
            ))}
            {loading && (
              <div className="chat-bubble chat-bubble-ai" style={{ opacity: 0.7 }}>
                Searching answers...
              </div>
            )}
            <div ref={messagesEndRef} />
          </div>

          <form onSubmit={handleSend} className="chat-input-area">
            <input 
              type="text" 
              className="form-control" 
              placeholder="Ask about Udaipur, Goa, or room pricing..." 
              value={input} 
              onChange={(e) => setInput(e.target.value)} 
            />
            <button type="submit" className="btn btn-primary btn-sm" disabled={loading}>
              <Send className="w-4 h-4" />
            </button>
          </form>
        </div>
      )}
    </>
  );
};

export default AiChatWidget;
