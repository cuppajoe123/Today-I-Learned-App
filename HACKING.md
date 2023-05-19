# Self-hosting Today I Learned
Today I Learned is completely self-hostable, which is one of the reasons behind the open source license. Self-hosting your own instance allows you to set the subject matter to something more specific than [today-i-learned.xyz](https://today-i-learned.xyz).

However, [today-i-learned.xyz](https://today-i-learned.xyz) benefits from some external APIs, such as Algolia for the search feature, as well as an email address for sending account verification emails. This document outlines the necessary changes one must make to the configuration files in order to successfully host Today I Learned.

## Database
It is currently recommended to self-host Today I Learned using the dev configuration profile. This eliminates the need to set up a separate database container, as it uses the H2 in-memory database, which should be sufficient.

## Algolia Search
The search feature is powered by Algolia. When submissions are made, they are uploaded to Algolia. To use Algolia, create an account there and follow their tutorials to create an index, which is where submissions are stored.

In `src/main/java/resources/application.yml`, copy your Algolia application ID into the field named `algolia-application-id`. You also need to copy the name of your index into `algolia-index-name`.

Next, rename `src/main/java/resources/secrets.example.yml` to `src/main/java/resources/secrets.yml`. This is where critical credentials are kept that are unsafe to store in a Git repository. Copy the Algolia indexing API key into the field `algolia-indexing-api-key`. All of these keys can be found in the Algolia settings.

Lastly, in `src/main/java/resources/static/search/src/dev-app.js`, replace the declaration on line 3 with `
const searchClient = algoliasearch(<application-id>, <search-only-API-key>);`
Also replace the index name with your index name on line 6.

This application was not originally meant to be self-hosted, and it requires external APIs, which is why this part was so cumbersome :|

## Email
Email is used to confirm registered accounts. The settings for using Gmail are already in `application.yml`. Alternatively, copy over the SMTP server and port of your provider. Then copy your actual email address into the `username` field in `application.yml`. Copy your password into the `password` field in `secrets.yml`. 

The confirmation email that is sent to new users contains a link, the server address. In the `domain-name` field of `application.yml`, either paste in a domain name that works on your network or the IP address of the server the application will be running on.

## Building and running the container
To build the Docker container image, run `./mvnw spring-boot:build-image -Dmaven.test.skip=true`

To start the container, run `docker compose up today-i-learned -d`


## The Codebase
You are welcome to hack at the code of this project, although it may not be the best code you've every seen ¯\_(ツ)_/¯.
You may notice that there are several Algolia-specific keys lying around the codebase. These only allow querying Algolia, not changing any sensitive data. I was too lazy to move them somewhere else, but they are not a security risk.