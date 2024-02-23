module.exports = {
  "xname": "Flexbox",
  "props": {
    "align": "start",
    "direction": "column"
  },
  "presenter": {
    "children": [
      {
          "container": {
            "xname": "TitledContainer",
            "props":{
              "title": "Classics"
            }
          },
          "presenter": {
            "xname": "Gridbox",
            "props": {
              "columns": 8,
            },
            "cart": {
              "xname": "Cart",
              "props": {
                "padding": "0px",
                "margin": "0px",
                "linewidth": "0",
                "corner": "8px"
              },
            },
            "binding": {
              "__indicator2": "__indicator",
              "__selector2": "__selector",
            },
            "container": "SelectList",
            "mock": [
              {
                "id": 11,
                "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2F675515b8-83d8-45e0-a3ec-7e8c8366ced0%2Fthumb_7C11634D-9BB5-4D1C-932B-06C176EEADE0.jpg"
              },
              {
                "id": 21,
                "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2Ff89f434f-9bff-4dd2-99a3-0a724a2f1884%2Fthumb_BE5632B0-CC58-42EC-9A0D-232F0FE8F833.jpg"
              },
              {
                "id": 31,
                "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2Ff897a52f-ad76-4353-a06d-63a1629294c0%2Fthumb_71558C40-0569-44D5-9F12-AA8F427C5D5D.jpg"
              },
              {
                "id": 41,
                "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2F6237b4a2-b067-44a2-9536-9e9387073855%2Fthumb_F5370FEF-1238-4082-843F-95BA3B23138D.jpg"
              }
            ],
            "presenter": {
              "xname": "DefaultAvatar",
              "props": {
                "size": 120
              }
            },
            "indicator": {},
            "selector": {}
          }
      },

      {
        "container": {
          "xname": "TitledContainer",
          "props":{
            "title": "Happy Birthday"
          }
        },
        "presenter": {
          "xname": "Gridbox",
          "props": {
            "column": 8
          },
          "cart": {
            "xname": "CssCart",
            "props": {
              "style":{
                "padding": "0px",
                "margin": "0",
                "borderRadius": '8px',
                "overflow": 'hidden',
              }
            }
          },
          "binding": {
            "__indicator2": "__indicator",
            "__selector2": "__selector",
          },
          "container": "SelectList",
          "mock": [
            {
              "id": 11,
              "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2Fb6527ee8-ddf9-4e9f-95cd-e0a67db89fed%2Fthumb_d9fa6747-63fe-4841-988a-9790306439ec.jpg",
            },
            {
              "id": 21,
              "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2Fafff652a-7c82-4130-a108-614300a62795%2Fthumb_8c2f0814-57a5-4916-90fe-c05122751dcc.jpg",
            },
            {
              "id": 31,
              "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2Fa7257436-d3e4-4dad-84c9-7a3cccb6ec3c%2Fthumb_c5dcc05b-fdf2-4c1d-997e-9d3afb725ca3.jpg",
            },
            {
              "id": 41,
              "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2F4350232e-724e-4408-9597-f837db06bd50%2Fthumb_c5754bf5-2015-45e2-831c-4396d9459238.jpg",
            }
          ],
          "presenter": {
            "xname": "ImageSize",
            "props": {
              "width": 144,
            }
          },
          "indicator": {},
          "selector": {}
        }
      },

      {
        "container": {
          "xname": "TitledContainer",
          "props": {
            "title": "质量等级",
          }
        },
        "presenter": {
          "xname": "Gridbox",
          "props": {
            "columns": 5,
          },
          "cart": {
            "xname": "Cart",
            "props": {
              "padding": "12px 70px 12px 12px",
              "margin": "0",
              "linewidth": "0px",
              "corner": "8px",
              "fill": "#1E2128"
            }
          },
          "binding": {
            "__indicator2": "__indicator",
            "__selector2": "__selector",
          },
          "container": "SelectList",
          "mock": [
            {
              "id": 11,
              "text": "超低质量",
              "subtext": "参数 0.25"

            },
            {
              "id": 21,
              "text": "低质量",
              "subtext": "参数 0.5"
            },
            {
              "id": 31,
              "text": "普通质量",
              "subtext": "参数 1"
            }
          ],
          "presenter": {
            "children": [
              {
                "presenter": {
                  "xname": "Text",
                  "props": {
                    "content": "Presenter",
                    "fontSize": "18px",
                    "color": "#fff",
                    "marginBottom": "3px",
                  },
                },
                "gateway": {
                  "xname": "Binding",
                  "props": {
                    "binding": {
                      "text": "content"
                    }
                  }
                }
              },
              {
                "presenter": {
                  "xname": "Text",
                  "props": {
                    "fontSize": "16px",
                    "color": "#8E8877"
                  },
                },
                "gateway": {
                  "xname": "Binding",
                  "props": {
                    "binding": {
                      "subtext": "content"
                    }
                  }
                }
              }
            ],
          },
          "indicator": {},
          "selector": {}
        }
      },

      {
        "container": {
          "xname": "TitledContainer",
          "props": {
            "title": "文字",
          }
        },
        "presenter": {
          "xname": "Gridbox",
          "props": {
            "columns": 5,
          },
          "cart": {
            "xname": "Cart",
            "props": {
              "padding": "6px 40px",
              "margin": "0",
              "linewidth": "1px",
              "corner": "8px"
            }
          },
          "binding": {
            "__indicator2": "__indicator",
            "__selector2": "__selector",
          },
          "container": "SelectList",
          "mock": [
            {
              "id": 11,
              "content": "1"
            },
            {
              "id": 21,
              "content": "2"
            },
            {
              "id": 31,
              "content": "3"
            },
            {
              "id": 41,
              "content": "4"
            }
          ],
          "presenter": {
            "xname": "Text",
            "props": {
            },
          },
          "indicator": {},
          "selector": {}
        }
      },
    ]
  }
}