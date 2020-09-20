const express = require('express');
const bodyParser = require('body-parser');
const crypto = require('crypto');
const app = express();
const port = 80;

const {Pool} = require('pg');

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

app.post('/like', (req, res) => {
    const values = [req.body.sender, req.body.receiver, req.body.value];
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query(
            'INSERT INTO matches (sender, receiver, match) VALUES ($1, $2, $3)',// ON CONFLICT sender, receiver SET match = $3',
            values,
            (err, reso) => {
                if (err) {
                    res.sendStatus(500)
                    console.log(err.stack);
                }
                res.send({"success": true});
                done();
            }
        );
    });
});

app.post('/login', (req, res) => {
    const values = [req.body.username, req.body.password];
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

                        client.query('INSERT INTO authorizations (username, token) VALUES ($1, $2) ON CONFLICT DO NOTHING',
                            [req.body.username, hash],
                            (err, reso) => {
                                if (err) {
                                    console.log(err.stack);
                                    res.sendStatus(500)
                                }
                            }
                        )

                        res.send({"success": true});
                    } else {
                        res.sendStatus(403)
                        // res.send({"success": false, "error": "Invalid user or password"});
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
        client.query('SELECT * FROM users WHERE username = $1', [req.params.uname], (err, reso) => {
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
                    covid: reso.rows[0].covid,
                    photos: reso.rows[0].photos
                };
                res.send(user);
            }
        });
    });
});

app.get('/getpotentialmatches/:uname', (req, res) => {
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query('SELECT * FROM users',// JOIN matches ON username = $1 or (username = ANY(ARRAY[sender, receiver]) AND $1 = ANY(ARRAY[sender, receiver]) AND match = true)',
            [], //[req.params.uname],
            (err, reso) => {
            done();
            if (err) {
                console.log(err.stack);
            } else {
                let uprofile = undefined;
                for (let i = 0; i < reso.rows.length; i++) {
                    if (reso.rows[i].username == req.params.uname) {
                        uprofile = reso.rows[i];
                        break;
                    }
                }

                let potmatches = [];
                for (let i = 0; i < reso.rows.length; i++) {
                    const user = {
                        username: reso.rows[i].username,
                        display_name: reso.rows[i].display_name,
                        age: reso.rows[i].age,
                        gender: reso.rows[i].gender,
                        favorites: reso.rows[i].favorites,
                        allergens: reso.rows[i].allergens,
                        covid: reso.rows[i].covid,
                        photo: reso.rows[i].photo
                    };

                    for (let j = 0; j < user.favorites.length; j++) {
                        if (uprofile.allergens.includes(user.favorites[j])) {
                            potmatches.push(user);
                            break;
                        }
                    }
                    if (potmatches.includes(user)) {
                        continue;
                    }

                    for (let j = 0; j < uprofile.favorites.length; j++) {
                        if (user.allergens.includes(uprofile.favorites[j])) {
                            potmatches.push(user);
                            break;
                        }
                    }
                }
                res.send(potmatches);
            }
        });
    });
});

app.get('/getmatchprofiles/:uname', (req, res) => {
    let users = []
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query(
            'SELECT match, sender, receiver FROM matches WHERE $1 = ANY(ARRAY[sender, receiver])',
            [req.params.uname],
            (err, reso) => {
                console.log(reso)
                if (err) {
                    console.log(err.stack);
                } else {
                    let dusers = []

                    function processEntry(entry) {
                        console.log(entry);
                        if (entry.sender === req.params.uname) {
                            return
                        }

                        if (!entry.match) {
                            return
                        }

                        for (let i = 0; i < reso.rows.length; i++) {
                            let inner = reso.rows[i];
                            console.log('asd', inner.sender, entry.receiver);

                            if (inner.sender === entry.receiver && entry.sender === inner.receiver) {
                                console.log("Made it in!")
                                if (inner.match && entry.match) {
                                    dusers.push(entry.sender)
                                }
                            }
                        }
                    }


                    reso.rows.forEach(processEntry)

                    client.query(
                        'SELECT * FROM users WHERE username = ANY($1)',
                        [dusers],
                        (err, reso) => {
                            if (err) {
                                // res.sendStatus(500)
                                console.log(err.stack);
                            } else {
                                let users = []
                                console.log(reso.rows)
                                reso.rows.forEach(row => [users.push({
                                    display_name: row.display_name,
                                    username: row.username,
                                    age: row.age,
                                    gender: row.gender,
                                    favorites: row.favorites,
                                    allergens: row.allergens,
                                    covid: row.covid,
                                    photos: row.photos
                                }), console.log(row.display_name)])
                                console.log(users)
                                res.send(users);
                            }
                            done();
                        });

                }
            }
        );
    });
});

app.get('/getmessages/:to/:from', (req, res) => {
    pool.connect((err, client, done) => {
        if (err) throw err;
        client.query(
            'SELECT from_user, timestamp, content FROM messages WHERE (from_user = $1 and to_user = $2) OR (from_user = $2 and to_user = $1) ORDER BY timestamp ASCENDING',
            [req.params.to, req.params.from],
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
