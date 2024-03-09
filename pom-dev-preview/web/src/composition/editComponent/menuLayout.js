module.exports = {
    xname: 'Gridbox',
    props: {
        columns: 1
    },
    mock: [
        { id: 1, name: 'presenter' },
        { id: 2, name: 'cart' },
        { id: 3, name: 'indicator' },
        { id: 4, name: 'selector' },
        { id: 5, name: 'container' }
    ],
    presenter: 'Text',
    binding: {
      "name": "content",
    },
    cart: {
      xname: 'Cart',
      props: {
        padding: '10px 40px',
        margin: '0',
        linewidth: '1px',
        corner: '8px',
      },
      // unselector: '', //默认样式
      indicator:
      {
        xname: 'ShadowIndicator',
        props: {
        }
      }, 
      selector: {
        xname: 'OutlineSelector',
        props: {
        }
      }
    },
    container: 'SelectList'
  };
  