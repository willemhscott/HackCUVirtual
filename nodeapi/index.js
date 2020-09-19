const express = require('express');
const app = express();
const port = 3000;

const { Pool } = require('pg');

const pool = new Pool({
    host: '3.17.77.33',
    user: 'ubuntu',
    password: 'password',
    database: 'hackcuvirtual',
    max: 20,
    idleTimeoutMillis: 30000,
    connectionTimeoutMillis: 2000
});

app.get('/users/:usernum', (req, res) => {
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query('SELECT username FROM users', (err, reso) => {
            done();
            if (err) {
                console.log(err.stack);
            } else {
                console.log(reso.rows[req.params.usernum]);
                res.send(reso.rows[req.params.usernum]);
            }
        });
    });
});

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`);
});
