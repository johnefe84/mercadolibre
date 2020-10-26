# mercadolibre
Creado por: John Franklin Ruiz Neira

Para poder hacer una prueba automatizada desde postman se puede usar la siguiente collection:

https://johnefe84-fuegodequasar.postman.co/workspaces/24cfd73c-be28-4668-85a9-4e7d1e392aa4/collections

Sin embargo, si se desea realizar la verificacion de manera manual se deben seguir las siguientes instrucciones:

Para poder probar los endpoint desarrollados, pero se debe tener en cuenta que a modo de plus he agregado validación con JWT utilizando Spring-Secuity. Por tal motivo, es necesario primero que nada obtener el token mediante el siguiente request:


curl --location --request POST 'http://mercadolibre-fuego-de-quasar.herokuapp.com/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"meli",
    "email":"user@gmail.com",
    "password":"password",
    "roles":[
        "ROLE_CLIENT"
    ]
}'


La respuesta del endpoint anterior retornara el token, supongamos que es “eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWxpIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9DTElFTlQifV0sImlhdCI6MTYwMjk5NjYzNCwiZXhwIjoxNjAyOTk2OTM0fQ.Qzs-Wu4ewuSBPI7p_WbHr7RfiwL7ZE2MbVl-paviqYw” 

1) Para probar el primer endpoint se debe utilizar el request como sigue:

curl --location --request POST 'http://mercadolibre-fuego-de-quasar.herokuapp.com/topsecret' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWxpIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9DTElFTlQifV0sImlhdCI6MTYwMjk5NjYzNCwiZXhwIjoxNjAyOTk2OTM0fQ.Qzs-Wu4ewuSBPI7p_WbHr7RfiwL7ZE2MbVl-paviqYw' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=2EBEB5AEE8900B9D453D6091A6268122' \
--data-raw '{
    "satellites":[
        {
            "nombre":"kenobi",
            "distancia":100.0,
            "message":["este","","","mensaje",""]
        }
        ,
        {
            "nombre":"skywalker",
            "distancia":115.5,
            "message":["","es","","","secreto"]
        }
        ,
        {
            "nombre":"sato",
            "distancia":142.7,
            "message":["este","","un","",""]
        }
    ]
}'

Ello me devolvera un response como sigue:
{
    "position": {
        "x": -487.28592,
        "y": 1557.0143
    },
    "message": "este es un mensaje secreto"
}

2) Para probar el siguiente endpoint que recibe de manera separada la informacion de cada satelite se deberan realizar 3 request como sigue:

curl --location --request POST 'http://mercadolibre-fuego-de-quasar.herokuapp.com/topsecret_split/kenobi' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWxpIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9DTElFTlQifV0sImlhdCI6MTYwMjk5NjYzNCwiZXhwIjoxNjAyOTk2OTM0fQ.Qzs-Wu4ewuSBPI7p_WbHr7RfiwL7ZE2MbVl-paviqYw' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=2EBEB5AEE8900B9D453D6091A6268122' \
--data-raw '       {
            "distancia":100.0,
            "message":["este","","","mensaje",""]
        }'


curl --location --request POST 'http://mercadolibre-fuego-de-quasar.herokuapp.com/topsecret_split/skywalker' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWxpIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9DTElFTlQifV0sImlhdCI6MTYwMjk5NjYzNCwiZXhwIjoxNjAyOTk2OTM0fQ.Qzs-Wu4ewuSBPI7p_WbHr7RfiwL7ZE2MbVl-paviqYw' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=2EBEB5AEE8900B9D453D6091A6268122' \
--data-raw '       {
            "distancia":115.5,
            "message":["","es","","","secreto"]
        }'

curl --location --request POST 'http://mercadolibre-fuego-de-quasar.herokuapp.com/topsecret_split/sato' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWxpIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9DTElFTlQifV0sImlhdCI6MTYwMjk5NjYzNCwiZXhwIjoxNjAyOTk2OTM0fQ.Qzs-Wu4ewuSBPI7p_WbHr7RfiwL7ZE2MbVl-paviqYw' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=2EBEB5AEE8900B9D453D6091A6268122' \
--data-raw '       {
            "distancia":142.7,
            "message":["este","","un","",""]
        }'

Luego que ya estan los 3 request se puede usar el GET para obtener la respuesta.

curl --location --request GET 'http://mercadolibre-fuego-de-quasar.herokuapp.com/topsecret_split'

Y la respuesta sera igual que en el punto 1)

{
    "position": {
        "x": -487.28592,
        "y": 1557.0143
    },
    "message": "este es un mensaje secreto"
}


En la siguiente Url es posible acceder al swagger del microservicio para poderlo probar desde allí

http://mercadolibre-fuego-de-quasar.herokuapp.com/swagger-ui.html
