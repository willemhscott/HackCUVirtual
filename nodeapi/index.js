const express = require('express');
const app = express();
const port = 3000;

const { Client } = require('pg');

const client = new Client({
    host: '3.17.77.33',
    port: 5432,
    user: 'ubuntu',
    password: 'password',
    database: 'hackcuvirtual'
});
client.connect((err) => {
    if (err) {
        console.error('error connecting', err.stack);
    } else {
        console.log('connected to ' + client.database);
    }
});

app.get('/users/:usernum', (req, res) => {
    client.query('SELECT username FROM users', (err, reso) => {
        client.end((err) => {
            console.log('client has disconnected');
            if (err) {
                console.log('error during disconnection', err.stack);
            }
        });
        res.send(reso.rows[req.params.usernum]);
    });
});

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`);
});
