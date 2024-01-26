module.exports = {
    xname: 'Gridbox',
    props: {
        columns: 8
    },
    presenter: {
    },
    binding: {
      "name": "content",
      'componentType': '___presenter.xname',
      'componentProps': '___presenter.props'
    },
    cart: {
      xname: 'Cart',
      props: {
        padding: '10px',
        margin: '1px 0',
        linewidth: 0,
        corner: '8px',
      },
  
      // unselector: '', //默认样式
      indicator:
      {
        xname: 'ShadowIndicator',
        props: {
        }
      }, //hover 时用, 第一次向子组件转递时,  更名为 hoverIndicator
      selector: {
        xname: 'SelectedCartUpperRightIcon',
        props: {
          state: 'selected',
          padding: '0'
        }
      }
    },
    container: 'SelectList'
  };
  