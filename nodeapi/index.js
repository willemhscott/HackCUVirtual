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

app.get('/users/:usernum', (req, res) => {
	(async () => {
		await client.connect((err) => {
			if (err) {
				console.error('error connecting', err.stack);
			} else {
				console.log('connected to ' + client.database);
			}
		});
		const reso = await client.query('SELECT username FROM users');
		await client.end();
		res.send(reso.rows[req.params.usernum]);
	})();
});

app.listen(port, () => {
	console.log(`Example app listening at http://localhost:${port}`);
});
