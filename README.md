# Today I Learned 
A website where users can read and share about new and
interesting things they have learned

## TODO
- Mitigate against spam and abuse:
    - Rate limiting (Algolia done, site wide rate limiting will be done via infrastructure)
- Add tests for email verification system
- Create a development and a production profile:
    - Manage secrets in more secure way
- Add upvote button to submissionById page (after launch)
- Forgot password (after launch)
- Add test for input sanitization

## Occurrences of development specific strings or secrets not to be used in production
- Must disable H2 Console in production
- SubmissionFormController: Algolia API key used in source code, as well as Algolia development indices
- VoteController: Algolia API key used in source code, as well as Algolia development indices
- DataLoader should not run in production

Test data is loaded from submission-bodies.json and submission-titles.json.
Currently, the first 45 records are used and uploaded to Algolia for search
functionality. If the amount of test data is changed, the Algolia index will
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
