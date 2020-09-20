const express = require('express');
const bodyParser = require('body-parser');
const crypto = require('crypto');
const app = express();
const port = 80;

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

app.get('/viewtable', (req, res) => {
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query('SELECT * FROM users', (err, reso) => {
            done();
            if (err) {
                console.log(err.stack);
            } else {
                res.send(reso.rows);
            }
        });
    });
});

app.get('/viewtablem', (req, res) => {
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query('SELECT * FROM messages', (err, reso) => {
            done();
            if (err) {
                console.log(err.stack);
            } else {
                res.send(reso.rows);
            }
        });
    });
});

app.post('/login', (req, res) => {
    const values = [ req.body.username, req.body.password ];
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query(
            'SELECT username FROM users WHERE username = $1 AND password = crypt($2, password)',
            values,
            (err, reso) => {
                if (err) {
                    console.log(err.stack);
                } else {
                    if (reso.rows[0]) {
                        const hash = crypto
                            .createHash('sha256')
                            .update(req.body.username + req.body.password, 'utf8')
                            .digest('base64');
                        res.header('X-Authorization', hash)
                        res.send({"success": true});
                    } else {
                        res.send({"success": false, "error": "Invalid user or password"});
                    }
                }
                done();
            }
        );
    });
});

app.get('/getprofile/:uname', (req, res) => {
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query('SELECT * FROM users WHERE username = $1', [ req.params.uname ], (err, reso) => {
            done();
            if (err) {
                console.log(err.stack);
            } else {
                const user = {
                    display_name: reso.rows[0].display_name,
                    age: reso.rows[0].age,
                    gender: reso.rows[0].gender,
                    favorites: reso.rows[0].favorites,
                    allergens: reso.rows[0].allergens,
                    covid: reso.rows[0].covid
                };
                res.send(user);
            }
        });
    });
});

app.get('/getmessages/:to/:from', (req, res) => {
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query(
            'SELECT from_user, timestamp, content FROM messages WHERE (from_user = $1 and to_user = $2) OR (from_user = $2 and to_user = $1)',
            [ req.params.to, req.params.from ],
            (err, reso) => {
                done();
                if (err) {
                    console.log(err.stack);
                } else {
                    res.send(reso.rows);
                }
            }
        );
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
    var taken = false;
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query('SELECT username FROM users', (err, reso) => {
            done();
            if (err) {
                console.log(err.stack);
            } else {
                reso.rows.forEach((uname) => {
                    if (uname.username == values[0]) {
                        res.send('Username already taken');
                        taken = true;
                    }
                });
            }
        });
    });
    pool.connect((err, client, done) => {
        if (err) throw err;
        if (!taken) {
            client.query(
                "INSERT INTO users (username, display_name, age, password, email_address, gender, favorites, allergens, covid) VALUES ($1, $2, $3, crypt($4, gen_salt('bf')), $5, $6, $7, $8, $9)",
                values,
                (err) => {
                    done();
                    if (err) {
                        console.log(err.stack);
                    } else {
                        res.sendStatus(200);
                    }
                }
            );
        }
    });
});

app.listen(port, () => {
    console.log(`App listening at http://localhost:${port}`);
});
