import Blockly from 'blockly';

const pad = (str, length = 20, char = ' ') =>
  str.padStart((str.length + length) / 2, char).padEnd(length, char);

function defBlock(name, fields, whenList, toolboxBlocks) {
    const p = 20;
    Blockly.Blocks[name] = {
      init: function () {
        var fld = new Blockly.Field(pad(name, p))
        var inp = this.appendDummyInput();
        inp.appendField(fld)
        inp.width = 200
        inp.setAlign(Blockly.ALIGN_CENTRE);
  
        this.appendValueInput("session")
          .setCheck(null)
          .appendField(" s:");
  
        // Todo: make this  work recuresively 
        function add(fields, obj) {
          fields.forEach(f => {
            if (f instanceof Object) {
              obj.appendValueInput(Object.keys(f)[0])
                .setCheck(null)
                .appendField(" " + Object.keys(f)[0] + ":")
  
              Blockly.Blocks[Object.keys(f)[0]] = {
                init: function () {
                  add(Object.values(f)[0], this);
                  this.setInputsInline(true);
                  this.setOutput(true, null);
                  this.setColour(230);
                }
              };
            }
            else {
              if (f.charAt(0) == "*") f = f.slice(1);
              obj.appendValueInput(f)
                .setCheck(null)
                .appendField(" " + f + ":")
            }
          })
        }
        add(fields, this);
  
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setInputsInline(true);
        this.setColour(285);
      },
    };
  
  
    ////////////////////////////////////////
    
    Blockly.JavaScript[name] = function (block) {
      let lowerCaseName = name.charAt(0).toLowerCase() + name.slice(1);
      var s = '\n' + lowerCaseName + ' \n ({ s:' + Blockly.JavaScript.valueToCode(block, "session", Blockly.JavaScript.ORDER_NONE) + ", ";
      function _add(fields, block) {
        let s = "";
  
        fields.forEach(f => {
          if (f instanceof Object) {
            s += (Object.keys(f)[0] + ": {" + _add(Object.values(f)[0], block.getInputTargetBlock(Object.keys(f)[0])) + "}, ")
          }
          else {
            if (f.charAt(0) == "*") f = f.slice(1);
            s += (f + ": " + Blockly.JavaScript.valueToCode(block, f, Blockly.JavaScript.ORDER_NONE)) + ", "
          }
        })
        return s;
      }
  
      s += _add(fields, block);
  
      s += " })\n";
      return s;
    };
  
    whenList.push([name, name]);
  
  
  
  
    // Build toolbox element -----
    var str = '';
    str += '    <block type="' + name + '">\n'
    str += '      <value name="session">\n'
    str += '        <block type="text">\n'
    str += '          <field name="TEXT"></field>\n'
    str += '        </block>\n'
    str += '      </value>\n'
  
    function recursiveAdd(fields) {
      fields.forEach(f => {
        if (f instanceof Object) {
          str += '<value name="' + Object.keys(f)[0] + '">\n';
          str += '<block type="' + Object.keys(f)[0] + '">\n';
          recursiveAdd(Object.values(f)[0]);
          str += '</block>\n';
          str += '</value>\n';
        } else {
          if (f.charAt(0) != "*") {
            str += '<value name="' + f + '">\n';
            str += '  <block type="text">\n';
            str += '    <field name="TEXT"></field>\n';
            str += '  </block>\n';
            str += '</value>\n';
          } else {
            f = f.slice(1);
            str += '<value name="' + f + '">\n';
            str += '    <block type="lists_create_with" inline="true"><mutation items="2"></mutation><value name="ADD0"><block type="text" id=";FwSclFxqGtB=w~7VU5h"><field name="TEXT"></field></block></value><value name="ADD1"><block type="text" id="fe.d$IJ7G$M]sg;w2T5!"><field name="TEXT"></field></block></value></block>'
            str += '</value>\n';
          }
        }
      })
    }
  
    recursiveAdd(fields)
  
    str += '    </block>'
    toolboxBlocks.push(str);
    //-----------------------------
  }

  export default defBlock;