const WebSocket = require('ws');
const { Pool } = require('pg');

const wss = new WebSocket.Server({
    port: 8080,
    perMessageDeflate: {
        zlibDeflateOptions: {
            // See zlib defaults.
            chunkSize: 1024,
            memLevel: 7,
            level: 3
        },
        zlibInflateOptions: {
            chunkSize: 10 * 1024
        },
        // Other options settable:
        clientNoContextTakeover: true, // Defaults to negotiated value.
        serverNoContextTakeover: true, // Defaults to negotiated value.
        serverMaxWindowBits: 10, // Defaults to negotiated value.
        // Below options specified as default values.
        concurrencyLimit: 10, // Limits zlib concurrency for perf.
        threshold: 1024 // Size (in bytes) below which messages
        // should not be compressed.
    }
});

const pool = new Pool({
    host: '3.17.77.33',
    user: 'ubuntu',
    password: 'password',
    database: 'hackcuvirtual',
    max: 20,
    idleTimeoutMillis: 30000,
    connectionTimeoutMillis: 2000
});

// {"type": "message", "from": "henry", "to": "abdul", "timestamp": 0, "content": "you have small ball"}

wss.on('connection', function connection(ws) {
    ws.on('message', function incoming(message) {
        const jmessage = JSON.parse(message);
        if (jmessage.type === 'message') {
            const values = [ jmessage.from, jmessage.to, jmessage.content ];
            pool.connect((err, client, done) => {
                if (err) throw err;
                client.query(
                    'INSERT INTO messages (from_user, to_user, content) VALUES ($1, $2, $3)',
                    values,
                    (err) => {
                        done();
                        if (err) {
                            console.log(err.stack);
                        }
                    }
                );
            });
        } else if (jmessage.type === 'authenticate') {
            const values = [ jmessage.username, jmessage.token ];
            pool.connect((err, client, done) => {
                if (err) throw err;
                client.query(
                    'SELECT token FROM authorizations WHERE username = $1 AND token = $2',
                    values,
                    (err) => {
                        done();
                        if (err) {
                            console.log(err.stack);
                        } else {
                            if (!reso.rows[0]) {
                                ws.close()
                            }
                        }
                    }
                );
            });
        }
        console.log(jmessage);
    });

    ws.send('connected');
});
