# A Simple Spreadsheet

### Shea O'Rourke

---
 
## Some general notes.

As Markdown is not standardized, this file is meant to be opened with Google Chrome.
 
---

## Class Structure

```
FunctionObject [I]
└ MultiToSingle

MapFunctionObject [I]
├ EvaluateCell
└ SetSelfReferentialError

Graph <K, V> [I]
└ ReferentialGraph <K, V>

Sexp [I]
├ SBoolean
├ SList
├ SNumber
├ SString
└ SSymbol

Parser

SexpVisitor <T> [I]
├ SexpToCellValue {CellValue<?>}
└ SexpToFormula {Formula}

FormulaVisitor [I]
└ EvaluateFormula

Formula [I]
├ AbstractCellFunction [A]
│ ├ ConcatCellFunction
│ ├ LessThanCellFunction
│ ├ ProductCellFunction
│ └ SumCellFunction 
├ AbstractCellReference [A]
│ ├ MultiReference
│ └ SingleReference
└ CellValue <T> [I]
  ├ AbstractCellValue <T> [A]
  │ └ PrimitiveCellValue <T> [A]
  │   ├ BooleanCellValue {Boolean}
  │   ├ DoubleCellValue {Double}
  │   └ StringCellValue {String}
  ├ CellValueError {String} [I]
  │ ├ DivByZeroError
  │ ├ EvaluationError
  │ ├ ParseSexpError
  │ └ SelfReferentialError
  └ EmptyCellValue

Cell [I]
├ FormulaCell
└ PrimitiveCell

CellFactory [I]
CellFunctionFactory [I]

WorksheetInterface [I]
└ Worksheet

[I] : interface
[A] : abstract class
<T> : generic type
{T} : type of generic type of parent class
```
 
 
 
---
 
## Graphs
 
For each cell, there is nothing inherent about its position. In other words, there is no
reason that each cell should be stored next to its visually adjacent cells in the data structure.
For this reason we have decided to store the cells inside a graph. The only cells in the graph
are the ones who contain information, and the edges represent references between the cells.

The list of nodes stored in the graph are stored in a HashTable, in our case with the key
as the coordinate and the value as the cell.

Some special functionality the graph interface provides include:
- checking to see if the graph contains cycles
- applying separate functions to cyclic disjoint subgraphs and tree disjoint subgraphs
- if the cell is in a cycle, apply a function to all cells in the cycle, and if not, applying
  a function to all itself and its ancestors from the bottom up

---

## S-Expressions

The S-Expression parser allows us to parse the string the user typed in to a form more appropriate
form for evaluating. Since S-Expressions don't have much if any direct meaning in terms of our
spreadsheet model, we turn S-Expressions into Formulas which are more relevant.

---

## Formulas

We store Formulas inside cells instead of S-Expressions because Formulas have more meaning in terms
of our model. A Formula is either a Cell Function, a Cell Reference, or a Cell Value.
Primitive Cells contain a Cell Value, while Formula Cells contain a Formula.

---

## Cells

Each cell keeps track of three things: the string the user typed into the cell, the formula 
associated with the string, and its value. Its value is inherently encoded within its formula, 
but recalculating each time we would like to access the value of a cell is inefficient, 
so we store the value separately and recalculate the value as needed. 

---

## Cell Values

We have objects that represent cell values so that we may get the value of the cell, and the return
type will be the Cell Value interface and not Object.
