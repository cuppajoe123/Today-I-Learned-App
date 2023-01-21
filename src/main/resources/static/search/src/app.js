const { algoliasearch, instantsearch } = window;

const searchClient = algoliasearch('0UGOGVIXV6', '43062c102da758e81e499dcd694b5dd2');

const search = instantsearch({
  indexName: 'dev_Submissions',
  searchClient,
});

search.addWidgets([
  instantsearch.widgets.searchBox({
    container: '#searchbox',
  }),
  instantsearch.widgets.hits({
    container: '#hits',
    templates: {
      item: `
<article>
  <h1><a href="/submission/{{objectID}}">{{#helpers.highlight}}{ "attribute": "title" }{{/helpers.highlight}}</a></h1>
  <span>By <a href="/user/{{#helpers.highlight}}{ "attribute": "author" }{{/helpers.highlight}}">{{#helpers.highlight}}{ "attribute": "author" }{{/helpers.highlight}}</a></span>
  <span> | </span>
  <span>On {{#helpers.highlight}}{ "attribute": "postedOn" }{{/helpers.highlight}}</span>
  <span> | </span>
  <span>{{#helpers.highlight}}{ "attribute": "points" }{{/helpers.highlight}} points</span>
</article>
`,
    },
  }),
  instantsearch.widgets.configure({
    hitsPerPage: 8,
  }),
  instantsearch.widgets.dynamicWidgets({
    container: '#dynamic-widgets',
    fallbackWidget({ container, attribute }) {
      return instantsearch.widgets.panel({ templates: { header: attribute } })(
        instantsearch.widgets.refinementList
      )({
        container,
        attribute,
      });
    },
    widgets: [
    ],
  }),
  instantsearch.widgets.pagination({
    container: '#pagination',
  }),
  instantsearch.widgets.sortBy({
    container: '#sort-by',
    items: [
      { label: 'Date (desc)', value: 'dev_Submissions_date_desc'},
      { label: 'Relevance', value: 'dev_Submissions'},
    ]
  }),
]);

search.start();
