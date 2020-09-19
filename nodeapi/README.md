Endpoints:
- sendmessage:
    - json body
    - from user
    - to user
    - content
    - ```postgresql
      INSERT INTO users (username, display_name, age, password, email_address, gender, favorites, allergens, covid) VALUES (
          $1, $2, $3, crypt($4, gen_salt('bf')), $5, $6, $7, $8, $9
      )
      ```
- getprofile:
    - username
    - display name
    - description
    - likes
    - allergic
    - corona
- messagehistory
    - ```postgresql
    SELECT from_user, timestamp, content FROM messages WHERE (from_user = $1 and to_user = $2) OR (from_user = $2 and to_user = $1)
    ```
- createuser
    - ```postgresql
    
    ```
