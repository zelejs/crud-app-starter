module.exports = {
    xname: 'Flexbox',
    props: {
      align: 'start',
      direction: 'row',
    },
    binding: {
      "propValue": "content",
    },
    presenter: {
      xname: 'Text',
      props: {
      },
   },
    cart: {
      xname: 'Cart',
      props: {
        padding: '10px',
        margin: '0 10px 10px 0',
        linewidth: 0,
        corner: '8px',
        fill: '#edf2f7'
      },
  
      unselector: "", //默认样式
      indicator:
      {
        xname: "",
        props: {
        }
      }, //hover 时用, 第一次向子组件转递时,  更名为 hoverIndicator
      selector: {
        xname: "",
        props: {
          state: 'selected',
          padding: '0'
        }
      }
    },
    container: 'SelectList'
  };
  