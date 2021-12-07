# Reading Is Good

## Container setup

### Mongo Container

Mongodb image needs to pulled if there is no image on local environment,
* docker pull mongo:latest

* docker run -d -p 27017:27017 --name reading-is-good-mongodb mongo:latest

### Application Container
We do have Dockerfile so,
* docker build -t reading-is-good-app:riga .
Link with Mongodb container and run app container
* docker run -p 8080:8080 --name riga-container --link reading-is-good-mongodb:mongo -d reading-is-good-app:riga
