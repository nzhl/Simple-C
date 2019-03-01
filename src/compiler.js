
/* ------- PART1 LEX (code => tokens) ---------------- */

function lex (code) {
  function space () {
    const WhiteSpaceChars = ' \t\r'
    while (true) {
      if (curIndex < code.length) {
        if (code[curIndex] === '\n') {
          curIndex++
          curRow++
          curColumn = 1
          continue
        } else if (WhiteSpaceChars.indexOf(code[curIndex]) !== -1) {
          curIndex++
          curColumn++
          continue
        }
      }
      break
    }
  }

  function comment () {
    // comment starts with '--'
    curIndex++
    curColumn++
    while (true) {
      curIndex++
      if (code[curIndex] === '\n') {
        curRow++
        curColumn = 1
        curIndex++
        return
      }
      curColumn++
    }
  }

  function string () {
    // string starts with '"'
    const token = {
      type: 'STRING',
      row: curRow,
      column: curColumn,
      value: ''
    }
    curIndex++
    curColumn++
    let isEscape = false
    while (true) {
      if (curIndex === code.length) {
        throw new Error(`Unfinished String at 行${token.row}, 列${token.column}`)
      }
      if (isEscape) {
        isEscape = false
        if (code[curIndex] === '\n') {
          curRow++
          curColumn = 1
          token.value = token.value.substr(0, token.value.length - 1)
        } else {
          token.value += code[curIndex++]
          curColumn++
        }
      } else {
        if (code[curIndex] === '\n') {
          throw new Error(`Unfinished String at 行${token.row}, 列${token.column}`)
        }
        if (code[curIndex] === '"') {
          curIndex++
          curColumn++
          return token
        }
        if (code[curIndex] === '\\') {
          isEscape = true
        }
        token.value += code[curIndex++]
        curColumn++
      }
    }
  }

  function number () {
    const token = {
      type: 'NUMBER',
      row: curRow,
      column: curColumn
    }
    let num = code.charCodeAt(curIndex) - 48
    while (true) {
      curIndex++
      curColumn++
      if (code[curIndex] >= '0' && code[curIndex] <= '9') {
        num = num * 10 + code.charCodeAt[curIndex] - 48
      } else {
        token.value = num
        return token
      }
    }
  }

  function id () {
    const token = {
      type: 'ID',
      row: curRow,
      column: curColumn,
      value: ''

    }
    while (true) {
      if ((code[curIndex] >= 'a' && code[curIndex] <= 'z') ||
          (code[curIndex] >= 'A' && code[curIndex] <= 'Z') ||
          (code[curIndex] >= '0' && code[curIndex] <= '9') ||
           code[curIndex] === '_') {
        token.value += code[curIndex++]
        curColumn++
      } else {
        return token
      }
    }
  }

  const tokens = []
  let curIndex = 0
  let curRow = 1
  let curColumn = 1

  while (true) {
    space()
    if (curIndex === code.length) {
      return tokens
    }
    switch (code[curIndex]) {
      case ';': case ',':
      case '{': case '}':
      case '[': case ']':
      case '(': case ')':
      case ':':
        tokens.push({
          type: 'PUNCTUATION',
          row: curRow,
          column: curColumn++,
          value: code[curIndex++]
        })
        break
      case '+': case '@':
      case '*': case '/':
      case '~': case '.':
        tokens.push({
          type: 'OPERATOR',
          row: curRow,
          column: curColumn++,
          value: code[curIndex++]
        })
        break
      case '-':
        if (code[curIndex + 1] === '-') {
          comment()
        } else {
          tokens.push({
            type: 'OPERATOR',
            row: curRow,
            column: curColumn++,
            value: code[curIndex++]
          })
        }
        break
      case '=':
        if (code[curIndex + 1] === '>') {
          tokens.push({
            type: 'OPERATOR',
            row: curRow,
            column: curColumn,
            value: '=>'
          })
          curIndex += 2
          curColumn += 2
        } else {
          tokens.push({
            type: 'OPERATOR',
            row: curRow,
            column: curColumn++,
            value: code[curIndex++]
          })
        }
        break
      case '>': case '<':
        if (code[curIndex + 1] === '=') {
          tokens.push({
            type: 'OPERATOR',
            row: curRow,
            column: curColumn,
            value: code[curIndex] + '='
          })
          curIndex += 2
          curColumn += 2
        } else if (code[curIndex] === '<' && code[curIndex + 1] === '-') {
          tokens.push({
            type: 'OPERATOR',
            row: curRow,
            column: curColumn,
            value: '<-'
          })
          curIndex += 2
          curColumn += 2
        } else {
          tokens.push({
            type: 'OPERATOR',
            row: curRow,
            column: curColumn++,
            value: code[curIndex++]
          })
        }
        break
      case '"':
        tokens.push(string())
        break
      default:
        if (code[curIndex] >= '0' && code[curIndex] <= '9') {
          tokens.push(number())
        } else if (
          (code[curIndex] >= 'a' && code[curIndex] <= 'z') ||
          (code[curIndex] >= 'A' && code[curIndex] <= 'Z') ||
          (code[curIndex] >= '0' && code[curIndex] <= '9') ||
           code[curIndex] === '_') {
          tokens.push(id())
        } else {
          throw new Error(`Unexpected character at 行${curRow}, 列${curColumn}`)
        }
    }
  }
}

/* ------- PART2 PARSE (tokens to AST) ---------------- */

function parse (tokens) {
  function _class () {
    // starts with "class"
    if (tokens[curIndex].type !== 'ID' || tokens[curIndex].value !== 'class') {
      throw new Error(`Missing keyword 'class' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
    }
    curIndex++
    const className = id()
    let parentClass = null
    let attributes = {}
    let methods = {}

    // "inherits" is optional
    if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'inherits') {
      curIndex++
      parentClass = id()
    }

    // {
    if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== '{') {
      throw new Error(`Missing punctuation '{' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
    }
    curIndex++

    while (tokens[curIndex].type === 'ID') {
      let name = id()
      if (tokens[curIndex].type === 'PUNCTUATION' && tokens[curIndex].value === ':') {
        // attribute definition e.g. `x: int <- 3`
        curIndex++
        const type = id()
        let value = null
        if (tokens[curIndex].type === 'PUNCTUATION' && tokens[curIndex].value === '<-') {
          curIndex++
          value = expression()
        }
        attributes[name] = { type, value }
      } else {
        // method definition
        const params = []
        let returnType = null
        let functionBody = null

        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== '(') {
          throw new Error(`Missing punctuation '(' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++

        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ')') {
          //  the method has parameters
          while (true) {
            const param = parameter()
            params.push(param)
            if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ',') {
              break
            }
            curIndex++
          }
        }
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ')') {
          throw new Error(`Missing punctuation ')' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ':') {
          throw new Error(`Missing punctuation ')' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++
        returnType = id()
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== '{') {
          throw new Error(`Missing punctuation '{' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++
        functionBody = expression()
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== '}') {
          throw new Error(`Missing punctuation '}' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++

        methods[name] = { returnType, params, functionBody }
      }
      // both matter method or attribute should end with ;
      if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ';') {
        throw new Error(`Missing punctuation ';' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
      curIndex++
    }
    if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== '}') {
      throw new Error(`Missing punctuation '}' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
    }
    curIndex++
    if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ';') {
      throw new Error(`Missing punctuation ';' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
    }
    curIndex++
    return { className, parentClass, attributes, methods }
  }

  function parameter () {
    const name = id()
    if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ':') {
      throw new Error(`Missing punctuation ':' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
    }
    curIndex++
    const type = id()
    return { name, type }
  }
  // just a string
  function id () {
    if (tokens[curIndex].type !== 'ID') {
      throw new Error(`Missing ID (type/name/keyword) at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
    }
    curIndex++
    return tokens[curIndex - 1].value
  }

  // priority rank: expression < expression1 < expression2 < expression3 < expression4
  function expression () {
    let expr = expression1()
    while (true) {
      if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '<=') {
        curIndex++
        expr = {
          type: 'LE',
          left: expr,
          right: expression1()
        }
      } else if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '<') {
        curIndex++
        expr = {
          type: 'LT',
          left: expr,
          right: expression1()
        }
      } else if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '>=') {
        curIndex++
        expr = {
          type: 'GE',
          left: expr,
          right: expression1()
        }
      } else if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '>') {
        curIndex++
        expr = {
          type: 'GT',
          left: expr,
          right: expression1()
        }
      } else if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '=') {
        curIndex++
        expr = {
          type: 'EQ',
          left: expr,
          right: expression1()
        }
      } else {
        break
      }
    }
    return expr
  }

  function expression1 () {
    let expr = expression2()
    while (true) {
      if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '+') {
        curIndex++
        expr = {
          type: 'ADD',
          left: expr,
          right: expression2()
        }
      } else if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '-') {
        curIndex++
        expr = {
          type: 'MINUS',
          left: expr,
          right: expression2()
        }
      } else {
        break
      }
    }
    return expr
  }

  function expression2 () {
    let expr = expression3()
    while (true) {
      if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '*') {
        curIndex++
        expr = {
          type: 'TIMES',
          left: expr,
          right: expression3()
        }
      } else if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '/') {
        curIndex++
        expr = {
          type: 'DIVISION',
          left: expr,
          right: expression3()
        }
      } else {
        break
      }
    }
    return expr
  }

  function expression3 () {
    // expr[@type][.ID([expr (,expr)*])]
    let expr = expression4()
    while (true) {
      let asType = null
      if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '@') {
        curIndex++
        asType = id()
      }

      if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '.') {
        curIndex++
        const methodName = id()
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== '(') {
          throw new Error(`Missing punctuation '(' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++

        const _arguments = []
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ')') {
          while (true) {
            _arguments.push(expression())
            if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ',') {
              break
            }
            curIndex++
          }
        }
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ')') {
          throw new Error(`Missing punctuation ')' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++
        expr = { type: 'INVOCATION', caller: expr, asType, methodName, _arguments }
      } else {
        break
      }
    }
    return expr
  }

  function expression4 () {
    let expr = null
    if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'if') {
      // if expr then expr else expr fi
      curIndex++
      const condition = expression()
      if (tokens[curIndex].type !== 'ID' || tokens[curIndex].value !== 'then') {
        throw new Error(`Missing 'then' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
      curIndex++
      const thenBody = expression()
      if (tokens[curIndex].type !== 'ID' || tokens[curIndex].value !== 'else') {
        throw new Error(`Missing 'else' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
      curIndex++
      const elseBody = expression()
      if (tokens[curIndex].type !== 'ID' || tokens[curIndex].value !== 'fi') {
        throw new Error(`Missing 'fi' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
      curIndex++
      expr = {
        type: 'IF',
        condition,
        thenBody,
        elseBody
      }
    } else if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'while') {
      // while expr loop expr pool
      curIndex++
      const condition = expression()
      if (tokens[curIndex].type !== 'ID' || tokens[curIndex].value !== 'loop') {
        throw new Error(`Missing 'loop' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
      curIndex++
      const loopBody = expression()
      if (tokens[curIndex].type !== 'ID' || tokens[curIndex].value !== 'pool') {
        throw new Error(`Missing 'pool' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
      curIndex++
      expr = {
        type: 'WHILE',
        condition,
        loopBody
      }
    } else if (tokens[curIndex].type === 'PUNCTUATION' && tokens[curIndex].value === '{') {
      // { (expr;)+ }
      curIndex++
      const exprs = []
      while (true) {
        exprs.push(expression())
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ';') {
          throw new Error(`Missing ';' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++
        if (tokens[curIndex].type === 'PUNCTUATION' && tokens[curIndex].value === '}') {
          curIndex++
          break
        }
      }
      expr = {
        type: 'EXPRESSIONS',
        expressions: exprs
      }
    } else if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'let') {
      // let ID : Type [<- expr ] (, ID : type [ <- expr ] )* in expr
      curIndex++
      const variables = {}

      while (true) {
        const name = id()
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ':') {
          throw new Error(`Missing ':' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
        }
        curIndex++
        const type = id()
        let value = null
        if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '<-') {
          curIndex++
          value = expression()
        }
        variables[name] = { type, value }
        if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ',') {
          break
        }
        curIndex++
      }
      if (tokens[curIndex].type !== 'ID' || tokens[curIndex].value !== 'in') {
        throw new Error(`Missing 'in' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
      curIndex++

      const letBody = expression()
      expr = {
        type: 'LET',
        variables,
        letBody
      }
    } else if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'new') {
      // new type
      curIndex++
      expr = {
        type: 'NEW',
        class: expression()
      }
    } else if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'isvoid') {
      //  isvoid expression
      curIndex++
      expr = {
        type: 'ISVOID',
        expression: expression()
      }
    } else if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '~') {
      //  isvoid expression
      curIndex++
      expr = {
        type: 'NEGATIVE',
        expression: expression()
      }
    } else if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'not') {
      //  isvoid expression
      curIndex++
      expr = {
        type: 'NOT',
        expression: expression()
      }
    } else if (tokens[curIndex].type === 'PUNCTUATION' && tokens[curIndex].value === '(') {
      //  (expr)
      curIndex++
      expr = {
        type: 'BRACKET',
        expression: expression()
      }
      if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ')') {
        throw new Error(`Missing punctuation ')' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
      curIndex++
    } else if (tokens[curIndex].type === 'NUMBER') {
      expr = {
        type: 'NUMBER',
        value: tokens[curIndex++].value
      }
    } else if (tokens[curIndex].type === 'STRING') {
      expr = {
        type: 'STRING',
        value: tokens[curIndex++].value
      }
    } else {
      if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'true') {
        curIndex++
        expr = {
          type: 'BOOLEAN',
          value: true
        }
      } else if (tokens[curIndex].type === 'ID' && tokens[curIndex].value === 'false') {
        curIndex++
        expr = {
          type: 'BOOLEAN',
          value: false
        }
      } else if (tokens[curIndex].type === 'ID') {
        const name = id()
        if (tokens[curIndex].type === 'OPERATOR' && tokens[curIndex].value === '<-') {
        // assign id <- expr
          curIndex++
          expr = {
            type: 'ASSIGN',
            name,
            value: expression()
          }
        } else if (tokens[curIndex].type === 'PUNCTUATION' && tokens[curIndex].value === '(') {
        // id([expr (, expr)*])
          curIndex++
          const _arguments = []
          if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ')') {
            while (true) {
              _arguments.push(expression())
              if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ',') {
                break
              }
              curIndex++
            }
          }
          if (tokens[curIndex].type !== 'PUNCTUATION' || tokens[curIndex].value !== ')') {
            throw new Error(`Missing ')' at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
          }
          curIndex++
          expr = {
            type: 'INVOCATION',
            caller: null,
            asType: null,
            methodName: name,
            arguments: _arguments
          }
        } else {
        // id
          expr = {
            type: 'VARIABLE',
            name
          }
        }
      } else {
        throw new Error(`Unexpected token at ${tokens[curIndex].row}, ${tokens[curIndex].column}`)
      }
    }
    return expr
  }

  const program = []
  let curIndex = 0

  // program is a set of classes
  while (curIndex !== tokens.length) {
    const c = _class()
    program.push(c)
  }
  return program
}

/* ------- PART3 translate (ast => js code) ---------------- */

function translate () {
  // TODO
}

function compile (code) {
  const tokens = lex(code)
  const ast = parse(tokens)
  console.log(ast)
  // const jsCode = translate(ast)
  // console.log(jsCode)
}

window.onload = () => {
  const btn = document.getElementById('btn')
  const input = document.getElementById('input')

  btn.onclick = () => {
    compile(input.value)
  }
}
