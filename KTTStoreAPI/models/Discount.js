const mongoose = require('mongoose');

const DiscountSchema = new mongoose.Schema({
    code: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    type: {
        type: String,
        enum: ['percent', 'fixed', 'shipping'],
        default: 'percent'
    },
    value: {
        type: Number,
        required: true
    },
    maxValue: {
        type: Number,
        default: 0
    },
    status: {
        type: String,
        enum: ['active', 'expired'],
        default: 'active'
    },
    usage: {
        type: Number,
        default: 0
    },
    usageLimit: {
        type: Number,
        default: 0
    },
    expiryDate: {
        type: Date
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Discount', DiscountSchema);

