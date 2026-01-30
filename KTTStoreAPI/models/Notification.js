const mongoose = require('mongoose');

const NotificationSchema = new mongoose.Schema({
    title: { type: String, required: true },
    type: { type: String, default: 'general' }, // welcome, promotion, policy, maintenance...
    status: {
        type: String,
        enum: ['pending', 'sent', 'expired'],
        default: 'sent'
    },
    content: { type: String },
    readCount: { type: Number, default: 0 },
    showAt: { type: Date },
    expireAt: { type: Date },
    createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Notification', NotificationSchema);

