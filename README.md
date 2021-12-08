# Reading Is Good

## Container setup

### Mongo Container

Please run given command in /mongodb folder;
* ``docker-compose up``

### Application Container
We do have Dockerfile so,
* ``docker build -t reading-is-good-app:riga .``

Link with Mongodb container and run reading-is-good-app container
* ``docker run -p 8080:8080 --name riga-container --network=mongodb_default --link mongodb:davybello/mongo-replica-set -d reading-is-good-app:riga``

### Postman Collection

First, ``Postman > collection`` JSON file needs to be imported to Postman.

Test username and password already given in Postman Collection. So please run;
* ``ReadingIsGood > Customer > Register`` HTTP request.

Login with registered login email and password. No need to write request body, it's already there.
* ``ReadingIsGood > Customer > Login``

JWT Token is defined as ``{{access_token}}`` in Postman so again (environment variables are given in ``Postman > env``), no need to copy token in order to request for calls that needs authentication.