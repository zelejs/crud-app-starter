module.exports = {
    xname: 'Flexbox',
    props: {
        align: 'start',
        direction: 'column'
    },
    cart: {
      xname: 'Cart',
      props: {
        padding: '10px',
        margin: '1px 0',
        linewidth: '1px',
        corner: '8px',
      },
    },
    container: 'PlainList',
    presenter: {
      xname: 'Flexbox',
      props: {
          align: 'between'
      },
      children: [
          {
            presenter: "Text",
            gateway: {
                xname: "Binding",
                props: {
                    binding: {
                      propName: "content"
                    }
                }
            }
          },
          {
            presenter: "SettingItem",
            gateway: {
                xname: "Binding",
                props: {
                    binding: {
                      propValue: "content"
                    }
                }
            }
          },
      ]
   },
  };
  