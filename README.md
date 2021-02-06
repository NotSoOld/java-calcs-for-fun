# java-calcs-for-fun
Just some Java practice. It's a simple calculator console app, which can handle integer and floating-point numbers and four main operations: addition, subtraction, multiplication and division. The main goal was to implement a robust object-oriented structure of the program which would allow to expand its capabilities easily.

Also, it has pretty weird mechanics to evaluate atomic operations (such as "token + token") as it really does not build any ASTs, but instead has a flat list of tokens and operates on that list exploiting the precedence of available operators. For example, consider the list of tokens:

[5, +, 3, *, 2]

Firstly, we'll evaluate the operators with most precendence: multiplication and division. We do have multiplication at position 3 in our list, so let's grab a sublist of the operator and its operands:

[3, *, 2]

Evaluate it - result will be 6 - and put back into the list where the first operand was:

[5, +, **6**]

As there are no more * or / operators, let's move to addition and subtraction. So, we perform the same subset of actions and get the answer, which will be a single number - 11.

For a long time I thought only about the standard way to evaluate expressions - you know, ASTs and other stuff - but the idea described above (and it originally was proposed by [Mavar3](https://github.com/Mavar3)) really makes me question if it can be used for more complex expressions, because came out to be so robust for the simple calculator.
