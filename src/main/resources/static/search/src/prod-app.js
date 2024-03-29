const { algoliasearch, instantsearch } = window;

const searchClient = algoliasearch('0UGOGVIXV6', '584be2e043e829c67de9c52e30bf7f9e');

const search = instantsearch({
  indexName: 'prod_Submissions',
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
      { label: 'Date (desc)', value: 'prod_Submissions_date_desc'},
      { label: 'Relevance', value: 'prod_Submissions'},
    ]
  }),
]);

search.start();
