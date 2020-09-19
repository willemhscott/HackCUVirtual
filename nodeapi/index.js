const express = require('express');
const app = express();
const port = 3000;

const { Client } = require('pg');

const client = new Client({
	host: 'localhost',
	port: 5432,
	user: 'ubuntu',
	password: 'password',
	database: 'hackcuvirtual'
});

(async () => {
	await client.connect((err) => {
		if (err) {
			console.error('error connecting', err.stack);
		} else {
			console.log('connected');
		}
	});
	console.log('connected to ' + client.database);
	// const res = await client.query('SELECT $1::text as message', [ 'Hello world!' ]);
	// console.log(res.rows[0].message); // Hello world!
	await client.end();
})();

app.get('/', (req, res) => {
	res.send('Hello World!');
});

app.listen(port, () => {
	console.log(`Example app listening at http://localhost:${port}`);
});
