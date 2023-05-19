# Today I Learned 
A website where users can read and share about new and
interesting things they have learned. For information on self-hosting, see HACKING.md.

## TODO
- Make repo public
- Allow editing of titles
- Mitigate against spam and abuse:
    - Rate limiting (Algolia done, site wide rate limiting will be done via infrastructure)
- Add tests for email verification system
- Add upvote button to submissionById page (after launch)
- Forgot password (after launch)
- Add test for input sanitization

## Algolia Info
Test data is loaded from submission-bodies.json and submission-titles.json.
Currently, the first 45 records are used and uploaded to Algolia for search
functionality in the dev environment. If the amount of test data is changed, the Algolia index will
also need to be changed.

Algolia currently keeps track of the following attributes for each submission:
- objectID
- author
- postedOn
- timestamp (UNIX format, used for sorting)
- title
- body
- points
 

- NOTE: when uploading records to Algolia, the UNIX timestamp is from exact
  Date+timestamp the submission was added, but the postedOn string uploaded to
  Algolia must have the timestamp information truncated, so it can be used on
  the front end. 

## Setting up the production server
- After spinning up the database container, the database must be manually created using psql: `CREATE DATABASE todayilearned;`
- Follow this site if SSL certs ever need to be regenerated: https://medium.com/@agusnavce/nginx-server-with-ssl-certificates-with-lets-encrypt-in-docker-670caefc2e31
- If the letsencrypt-nginx-container ever has to be used to get the SSL certs, make sure the .well-known directory already exists
- docker/production.conf is the production nginx configuration
- docker/nginx.conf is for setting up SSL certs for the first time
