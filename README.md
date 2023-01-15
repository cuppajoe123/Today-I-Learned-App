# Today I Learned A website where users can read and share about new and
  interesting things they have learned

## TODO
- Refactor to use the new Date Time API
- Fine tune search feature
- Implement colorscheme

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
