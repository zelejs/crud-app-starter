module.exports = {
    xname: 'Gridbox',
    props: {
        columns: 8
    },
    presenter: {
      xname: 'CommonItem',
      props: {
      },
    },
    binding: {
      "componentType": "label",
      "items": "value"
    },
    cart: {
      xname: 'Cart',
      props: {
        padding: '30px 10px',
        margin: '',
        linewidth: 0,
        corner: '8px',
        fill: '#edf2f7'
      },
  
      unselector: "", //默认样式
      indicator:
      {
        xname: "ShadowIndicator",
        props: {
        }
      }, //hover 时用, 第一次向子组件转递时,  更名为 hoverIndicator
      selector: {
        xname: "SelectedCartUpperRightIcon",
        props: {
          state: 'selected',
          padding: '0'
        }
      }
    },
    container: 'SelectList'
  };
  