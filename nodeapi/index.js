const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;

const { Pool } = require('pg');

app.use(bodyParser.json());

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

app.post('/createuser', (req, res) => {
    const values = [
        req.body.username,
        req.body.display,
        req.body.age,
        req.body.password,
        req.body.email,
        req.body.gender,
        JSON.stringify(req.body.favorites),
        JSON.stringify(req.body.allergens),
        req.body.covid
    ];
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query(
            "INSERT INTO users (username, display_name, age, password, email_address, gender, favorites, allergens, covid) VALUES ($1, $2, $3, crypt($4, gen_salt('bf')), $5, $6, $7, $8, $9)",
            values,
            (err, reso) => {
                done();
                if (err) {
                    console.log(err.stack);
                } else {
                    // console.log(reso.rows[req.params.usernum]);
                    // res.send(reso.rows[req.params.usernum]);
                }
            }
        );
    });
    res.sendStatus(200);
});

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`);
});
