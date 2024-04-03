module.exports = {
    xname: 'Gridbox',
    props: {
        columns: 1
    },
    mock: [
        { id: 1, name: 'element' },
        { id: 2, name: 'addnew' },
        { id: 3, name: 'card' },
        { id: 4, name: 'cart' },
        { id: 5, name: 'row' },
        { id: 6, name: 'selector' },
        { id: 7, name: 'tag' },
        { id: 8, name: 'user' },
        { id: 9, name: 'autolayout' }
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
  