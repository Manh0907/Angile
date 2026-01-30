const express = require('express');
const router = express.Router();
const Notification = require('../models/Notification');

// GET /api/notifications
router.get('/', async (req, res) => {
    try {
        const list = await Notification.find().sort({ createdAt: -1 });
        res.json(list);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// POST /api/notifications
router.post('/', async (req, res) => {
    try {
        const data = req.body;
        const noti = new Notification({
            title: data.title,
            type: data.type || 'general',
            status: data.status || 'sent',
            content: data.content,
            readCount: data.readCount || 0,
            showAt: data.showAt || null,
            expireAt: data.expireAt || null
        });
        await noti.save();
        res.status(201).json(noti);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// PUT /api/notifications/:id
router.put('/:id', async (req, res) => {
    try {
        const data = req.body;
        const update = {
            title: data.title,
            type: data.type,
            status: data.status,
            content: data.content,
            readCount: data.readCount,
            showAt: data.showAt,
            expireAt: data.expireAt
        };

        const noti = await Notification.findByIdAndUpdate(
            req.params.id,
            update,
            { new: true }
        );

        if (!noti) {
            return res.status(404).json({ msg: 'Notification not found' });
        }
        res.json(noti);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// DELETE /api/notifications/:id
router.delete('/:id', async (req, res) => {
    try {
        const noti = await Notification.findByIdAndDelete(req.params.id);
        if (!noti) {
            return res.status(404).json({ msg: 'Notification not found' });
        }
        res.json({ msg: 'Notification removed' });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

module.exports = router;

