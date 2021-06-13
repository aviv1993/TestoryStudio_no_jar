import defBlock from './blocks_gen'
import Blockly from 'blockly';

function defineBpBLocksJsTranslation(){
    Blockly.JavaScript['scenario'] = function (block) {
    const name = block.getFieldValue("NAME");
    const code = Blockly.JavaScript.statementToCode(block, "code");

    return "bthread('" + name + "', function () {\n" + code + "})\n"
    };

    Blockly.JavaScript['loop'] = function (block) {
    const n = block.getFieldValue("N");
    const code = Blockly.JavaScript.statementToCode(block, "CODE");

    return "\nfor (var i = 0; i < " + n + "; i++) {" + code + "}\n"
    };


    Blockly.JavaScript['StartSession'] = function (block) {
    const name = Blockly.JavaScript.valueToCode(block, "NAME", Blockly.JavaScript.ORDER_NONE);
    const url = Blockly.JavaScript.valueToCode(block, "URL", Blockly.JavaScript.ORDER_NONE);

    return "\nstartSession(" + name + ", " + url + ")\n";
    };

    Blockly.JavaScript['Title'] = function (block) {
    const desc = block.getFieldValue("DESC");

    return "testSuite('" + desc + "')\n";
    };


    Blockly.JavaScript['when'] = function (block) {
    const name = block.getFieldValue("NAME");
    const code = Blockly.JavaScript.statementToCode(block, "CODE");

    return "\nwhen(Any(" + name + "),function (e) {" + code + "})\n"
    };


    Blockly.JavaScript['block'] = function (block) {
    const name = block.getFieldValue("NAME");
    const code = Blockly.JavaScript.statementToCode(block, "CODE");

    return "\nblock(Any(" + name + "), function (e) {" + code + "})\n"
    };


    Blockly.JavaScript['field'] = function (block) {
    const name = block.getFieldValue("NAME");
    return ["e." + name, Blockly.JavaScript.ORDER_NONE];
    };


    Blockly.JavaScript['random'] = function () {
    return ["random.string(4)", Blockly.JavaScript.ORDER_NONE];
    };

    Blockly.JavaScript['choose'] = function (block) {
    const choices = Blockly.JavaScript.valueToCode(block, "CHOICES", Blockly.JavaScript.ORDER_NONE);
    return ["random.sample(" + choices + ")", Blockly.JavaScript.ORDER_NONE];
    };
}

function defineBpDefaultBlocks(){
    Blockly.Blocks["scenario"] = {
        init: function () {
        this.appendDummyInput()
            .appendField("Behaviour thread: ")
            .appendField(new Blockly.FieldTextInput("description"), "NAME");
        this.appendStatementInput("code").setCheck(null);
        this.setColour(230);
        this.setTooltip(
            "This block defines a scenario that run in parallel to the other scenarios"
        );
        this.setHelpUrl("");
        },
    };
    
    Blockly.Blocks['loop'] = {
        init: function () {
        this.appendDummyInput()
            .appendField("do")
            .appendField(new Blockly.FieldNumber(0, 0), "N")
            .appendField("times");
        this.appendStatementInput("CODE")
            .setCheck(null);
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setColour(230);
        this.setTooltip("Use this block to repeat things");
        this.setHelpUrl("");
        }
    };
    
    Blockly.Blocks['text'] = {
        init: function () {
        this.appendDummyInput()
            .appendField(new Blockly.FieldTextInput("text"), "TEXT");
        this.setOutput(true, null);
        this.setColour(230);
        this.setTooltip("A line of text");
        this.setHelpUrl("");
        }
    };
    
    Blockly.Blocks['field'] = {
        init: function () {
        this.appendDummyInput()
            .appendField("event.")
            .appendField(new Blockly.FieldTextInput("field name"), "NAME");
        this.setOutput(true, null);
        this.setColour(200);
        this.setTooltip("A field of the 'when' event");
        this.setHelpUrl("");
        }
    };
    
    Blockly.Blocks['random'] = {
        init: function () {
        this.appendDummyInput()
            .appendField("random string");
        this.setOutput(true, null);
        this.setColour(200);
        this.setTooltip("A random string generator");
        this.setHelpUrl("");
        }
    };
    ///////////////////////////////////////////////////////////////
    
    Blockly.Blocks['Title'] = {
        init: function () {
        this.appendDummyInput()
            .appendField(new Blockly.FieldTextInput("description"), "DESC");
        this.setInputsInline(true);
        this.setColour(200);
        this.setTooltip("Add title to the test suite");
        this.setHelpUrl("");
        }
    };
    ///////////////////////////////////////////////////////////////
    
    Blockly.Blocks['choose'] = {
        init: function () {
        this.appendValueInput("CHOICES")
            .setCheck(null)
            .appendField(" choose:");
        this.setOutput(true, null);
        this.setColour(200);
        this.setTooltip("A nondeterministic choice");
        this.setHelpUrl("");
        }
    };
    
    ///////////////////////////////////////////////////////////////
    
    const pad = (str, length = 20, char = ' ') =>
        str.padStart((str.length + length) / 2, char).padEnd(length, char);
    
    Blockly.Blocks['StartSession'] = {
        init: function () {
    
        var fld = new Blockly.Field(pad("Start session", 25))
        this.appendDummyInput().appendField(fld)
    
        this.appendValueInput("NAME")
            .setCheck(null)
            .appendField(" s:");
        this.appendValueInput("URL")
            .setCheck(null)
            .appendField(" url:");
        this.setInputsInline(true);
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setColour(285);
        this.setTooltip("Start a new browser session");
        this.setHelpUrl("");
        }
    };
}
defineBpDefaultBlocks()
defineBpBLocksJsTranslation()
  //////////////////////////////////////////////

var genBpToolBox = function(blocks) {
    var whenList = [];
    var toolboxBlocks = [];
    if (blocks){
        blocks.forEach(block => defBlock(block.eventName, block.eventVarDefinitions, whenList, toolboxBlocks));
    }
    var bpToolBox = `
    <xml>
    <category name="Structures" colour="%{BKY_LOGIC_HUE}">
        <block type="scenario"></block>
        <block type="Title"></block>
        <block type="field">
        <value name="NAME">
            <block type="text">
            <field name="TEXT"></field>
            </block>
        </value>
        </block>
        <block type="choose">
        <value name="CHOICES">
            <block type="lists_create_with" inline="true"><mutation items="2"></mutation><value name="ADD0"><block type="text" id=";FwSclFxqGtB=w~7VU5h"><field name="TEXT"></field></block></value><value name="ADD1"><block type="text" id="fe.d$IJ7G$M]sg;w2T5!"><field name="TEXT"></field></block></value></block>
        </value>
        </block>
        <block type="field">
        <value name="NAME">
            <block type="text">
            <field name="TEXT"></field>
            </block>
        </value>
        </block>


        </category>
        <category name="Sessions" colour="%{BKY_LOGIC_HUE}">
        <block type="StartSession">
        <value name="NAME">
            <block type="text">
            <field name="TEXT"></field>
            </block>
        </value>
        <value name="URL">
            <block type="text">
            <field name="TEXT"></field>
            </block>
        </value>
        </block>
    </category>
    `
    
    bpToolBox += '  <category name="Events" colour="%{BKY_LOGIC_HUE}">'
    toolboxBlocks.forEach(str => bpToolBox += str);
    bpToolBox += '  </category>'
    
    bpToolBox += `
    <category name="Logic" colour="%{BKY_LOGIC_HUE}">
    <block type="controls_if"></block>
    <block type="logic_compare"></block>
    <block type="logic_operation"></block>
    <block type="logic_negate"></block>
    <block type="logic_boolean"></block>
    </category> 
    <category name="Loops" colour="%{BKY_LOOPS_HUE}">
    <block type="loop"></block>
    <block type="controls_repeat_ext">
        <value name="TIMES">
        <block type="math_number">
            <field name="NUM">10</field>
        </block>
        </value>
    </block>
    <block type="controls_whileUntil"></block>
    </category>
    <category name="Math" colour="%{BKY_MATH_HUE}">
    <block type="math_number">
        <field name="NUM">123</field>
    </block>
    <block type="math_arithmetic"></block>
    <block type="math_single"></block>
    </category>
    <category name="Text" colour="%{BKY_TEXTS_HUE}">
    <block type="random"></block>
    <block type="text"></block>
    <block type="text_length"></block>
    <block type="text_join">
        <mutation items="2"></mutation>
    </block>
    </category>

    <category name="Lists" colour="260">
        <block type="lists_create_with">
        <mutation items="0"></mutation>
        </block>
        <block type="lists_create_with">
        <mutation items="3"></mutation>
        </block>
    </category>
    <category name="Lists" colour="#745ba5">
        <block type="lists_create_with">
        <mutation items="0"></mutation>
        </block>
        <block type="lists_create_with">
        <mutation items="3"></mutation>
        </block>
        <block type="lists_repeat">
        <value name="NUM">
            <shadow type="math_number">
            <field name="NUM">5</field>
            </shadow>
        </value>
        </block>
        <block type="lists_length"></block>
        <block type="lists_isEmpty"></block>
        <block type="lists_indexOf">
        <field name="END">FIRST</field>
        <value name="VALUE">
            <block type="variables_get">
            <field name="VAR" id="H|s*#.9]FwEL/j5V3fwA">list</field>
            </block>
        </value>
        </block>
        <block type="lists_getIndex">
        <mutation statement="false" at="true"></mutation>
        <field name="MODE">GET</field>
        <field name="WHERE">FROM_START</field>
        <value name="VALUE">
            <block type="variables_get">
            <field name="VAR" id="H|s*#.9]FwEL/j5V3fwA">list</field>
            </block>
        </value>
        </block>
        <block type="lists_setIndex">
        <mutation at="true"></mutation>
        <field name="MODE">SET</field>
        <field name="WHERE">FROM_START</field>
        <value name="LIST">
            <block type="variables_get">
            <field name="VAR" id="H|s*#.9]FwEL/j5V3fwA">list</field>
            </block>
        </value>
        </block>
        <block type="lists_getSublist">
        <mutation at1="true" at2="true"></mutation>
        <field name="WHERE1">FROM_START</field>
        <field name="WHERE2">FROM_START</field>
        <value name="LIST">
            <block type="variables_get">
            <field name="VAR" id="H|s*#.9]FwEL/j5V3fwA">list</field>
            </block>
        </value>
        </block>
        <block type="lists_split">
        <mutation mode="SPLIT"></mutation>
        <field name="MODE">SPLIT</field>
        <value name="DELIM">
            <shadow type="text">
            <field name="TEXT">,</field>
            </shadow>
        </value>
        </block>
        <block type="lists_sort">
        <field name="TYPE">NUMERIC</field>
        <field name="DIRECTION">1</field>
        </block>
    </category>

    <category name="Variables" colour="330" custom="VARIABLE"></category>
    </xml>`;
    return bpToolBox;
}

var genBlocklyOptions = function(blocks){
    return {
        media: 'media/',
        grid:
          {
            spacing: 25,
            length: 3,
            colour: '#ccc',
            snap: true
          },
        toolbox: genBpToolBox(blocks)
      }
}
export default genBlocklyOptions;
