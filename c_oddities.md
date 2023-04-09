# Quirks and Oddities of C

## Inventing C
C was invented so the programmers at Bell Labs wouldn’t have to write programs and operating systems in assembly.
Conceived around 1972, it wasn’t standardized until the 80s, leaving lots of literature and legacy code that uses nonstandard C.
Standards are important for normal programmers and compiler writers, so that they have an agreed upon set of rules.

The C standard evolves very slowly compared to other languages, meaning it isn’t bloated, but it also have many artifacts from the days of yore.
Along with that, the C used by developers varies by decades, as many common open source projects intentionally use old C versions for compatibility reasons (C99), or just make their own compiler modifications (ex. Linux).

## Standards and Behavior
Even with a standard, the C programming language still leaves lot up to interpretation. As such, there are many circumstances in which the specification is adapted. The main three types are:
- Implementation-defined behavior - (Depends on target,).
- Unspecified behavior - (“Correct” impl., ambiguous specification)
- Undefined behavior - (No correct behavior, no spec).

Anything can happen with undefined behavior, from the program crashing, to nuclear missiles being launched. Beware!

## Implentation-Defined Behavior
Implementation-defined behavior is derived from behavior that depends on the platform in which it is implemented or targeted towards. Documentation is your friend.
- Size of types (char on Cygwin vs. everywhere else).
- Whether or not a sign bit is propagated after a right bit shift.

## Unspecified Behavior
Unspecified behavior is behavior left ambiguous (multiple “correct” implementations) by the C specification. The behavior is not completely defined, though a rough consensus has been created. There’s some semblance of a “correct” behavior, albeit missing from specification.
- The order of argument evaluation is unspecified behavior.

## Undefined Behavior
Undefined behavior is specifically behavior that is left unspecified by the C Standard and also lacks any consensus (even within the same compiler). 
As such, the potential for strange and unknown behavior is possible, usually resulting in crashes and other unwanted side-effects.

Undefined behavior can be useful for compiler writers, because they can assume a specific behavior will never occur, meaning it can ignore it and optimize it out.
Using undefined behavior (knowingly or not) is dangerous and can cause unpredictable results, even if you think you know what will happen. It is all highly dependant on the hardware, compiler toolchain, and luck of the draw.

A simple example of undefined behavior is the signed integer overflow. 
This is left undefined for a few reasons. First, there is no universal specification for how signed integers should work. Are they two's complement? One’s complement? Etc.
The C Standard was created during the time before a consensus was reached (two’s complement), so it is up to the toolchain to implement some way of managing hardware-specific integer overflows. 
As such, the compiler usually ignores these circumstances and expects the developer to deal with it themselves.

### Other examples
- Modifying a string literal (literals are immutable).
- Division by zero. (Undefined by math itself…)
- Index out of bounds error. (What’s it supposed to do…?)
- Dereferencing null pointer. (Access nothing?)
- Unsequenced modifications to a variable (i++ + i++).

## Strange Language Features (that seem like bugs)
### Switch Statements
Switch Statements can be used instead of if statements. A value is checked against each case, and if they match, the following statement is executed.
One “bug” is that by default, statements fall through each other, hence the required break statement.
There is also the default case, which is optional and just a magic label, so misspelling it will cause an undetectable bug.
The real accursed creation with switch statements is the inclusion of goto.
This allows the program to skip to any label, breaking control-flow and inviting strange behavior. It does have real uses (for saving code), but there’s a reason many programming languages no longer include it…

    int main()
    {
        int i = 2;
        switch (i) {
            case 5+3: do_again:
            case 2: printf("Infinite loop!\n"); goto do_again;
            default: i++;
            case 3: /* default does not even need to be last */
        }
    }

### Functions are global by default
When you write a function, it can be called by any code that links to that file.
You have to specify a function is static to make it visible only to other functions in the same file.
Many programmers forget to do this, so the namespace gets clogged up. Visibility of functions are also binary. Its either visible to all code or the same file. 

### Function Keywords
`static` - File-Only

[none] - Any file linking can
call it.

`extern` -  Just another way to
specify that it is available to linked files. (Also makes sure to allocate space in compiled source to link to.)

    static void private_function() {
    }
    
    void global_function() {
    }
    
    extern void also_global_function() {
    }
    
### Overloading keywords and operators
Many of C’s keywords and operators have vastly different meanings in different contexts.

`static`: (1) Inside a function, it means a variable’s value persists across calls. (2) When modifying a function, it means the function is local to that file.

`void`: (1) As a return type of a function, it means the function returns no value. (2) In a pointer declaration, it means a generic, untyped pointer. (3) In a function argument list, it means no arguments.

    void foo(void) {
        void *pointer;
        /* do something */
        return;
    }
    
\* : (1) multiplication operator (2) When applied to a pointer, indirection (3) In a declaration, pointer.

& : (1) address-of operator (2) bitwise AND operator.

### A CTF Classic: gets()
Not all flaws are with the language itself. Standard library functions can have flaws too.
`char *gets(char *s)` simply gets a string from standard input and places in `s`.
`s` is a buffer of arbitrary size. In C, buffer/array overflow is not checked automatically.
If the input is larger than the size of s, then the buffer overflows into other parts of the program.
Result: SEGFAULT or data corruption.
gets() is often used in CTF challenges.
Use `char *fgets(char *s, int size, FILE stream)` instead.

### Type Promotions
Variables in C are associated with a type that tells you what they represent. For example `int`, `float`, `char`, etc.
`int`s are usually 4 bytes and chars are usually 1 byte.
But when `char`s are involved in an expression, they are promoted to `int`s.
Because of these weird rules, 
`printf(“%d”, sizeof ‘A’);` prints 4, not 1.
    
    char c1, c2;
    c1 = c1 + c2;

This code snippet promotes c1 and c2 to integers and then truncates them. 
Arguments are expressions, hence `putchar(int c)`, not `putchar(char c)`.

## But C is not all bad!
C is still a great language to use and learn from.
It assumes the programmer knows what they’re doing, so it won’t stop you from making a mistake (though the toolchain has improved a lot).
While not a low level language, it provides a very minimal set of facilities to the programmer, so one has to get creative. Less is more!
Interesting C codebases include operating system kernels, SQL databases, AI libraries, and more!
If you like to live dangerously, give C a chance.

I learned most of the information presented here from Expert C Programming: Deep C Secrets by Peter van der Linden, a book that provided many historical insights and anecdotes.
