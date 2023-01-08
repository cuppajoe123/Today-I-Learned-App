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
  <h1>{{#helpers.highlight}}{ "attribute": "title" }{{/helpers.highlight}}</h1>
  <p>{{#helpers.highlight}}{ "attribute": "author" }{{/helpers.highlight}}</p>
  <p>{{#helpers.highlight}}{ "attribute": "postedOn" }{{/helpers.highlight}}</p>
  <p>{{#helpers.highlight}}{ "attribute": "body" }{{/helpers.highlight}}</p>
  <p>{{#helpers.highlight}}{ "attribute": "points" }{{/helpers.highlight}}</p>
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
]);

search.start();
