module.exports = {
    xname: 'Gridbox',
    props: {
        columns: 8
    },
    binding: {
      'componentType': 'content',
    },
    cart: {
      xname: 'Cart',
      props: {
        padding: '30px 10px',
        margin: '1px 0',
        linewidth: '1px',
        corner: '8px',
        fill: '#edf2f7'
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
    container: 'SelectList',
    presenter: {
      xname: 'Text',
      props: {
      },
    },
  };
  