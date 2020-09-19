Endpoints:
- sendmessage:
    - post
    - json body
    - from user
    - to user
    - content
    - make sure users exist
    - at some point check that the user has auth to send that
    - ```postgresql
      INSERT INTO messages (from_user, to_user, content) VALUES ($1, $2, $3)
      ```
- getprofile:
    - get
    - username
    - display name
    - description
    - likes
    - allergic
    - corona
- messagehistory
    - get
    - ```postgresql
      SELECT from_user, timestamp, content FROM messages WHERE (from_user = $1 and to_user = $2) OR (from_user = $2 and to_user = $1)
      ```
- createuser
    - post
    - like sendmessage
    - error if user with username already exists
    - ```postgresql
      INSERT INTO users (username, display_name, age, password, email_address, gender, favorites, allergens, covid) VALUES (
                $1, $2, $3, crypt($4, gen_salt('bf')), $5, $6, $7, $8, $9
      )
      ```
    - ```json
      {
          "username": "john",
          "display": "John Smith",
          "age": 25,
          "password": "p4ssw0rd",
          "email": "john.smith@example.com",
          "gender": "male",
          "favorites": ["sushi"],
          "allergens": ["peanuts"],
          "covid": false
      }
      ```