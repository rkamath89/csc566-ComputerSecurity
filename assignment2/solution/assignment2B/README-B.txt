FIRSTNAME: Rahul
LASTNAME: Kamath

EMAILADDRESS: rkamath@email.arizona.edu

UNDERGRADUATE,MASTERS,PHD (U/M/P): M

PROBLEM (1/2): 1 and 2

TOOLS:
GDB,Pen and Paper, NotePad++, HexEdit

TECHNIQUES:
Used the various commands provided by GDB and traced the program crash, took objdump and analysed the instructions.

TIME:
2 Days

DIFFICULTY:
Hard

CHALLENGES:

For part 2B-1 i am getting the outputs as mentioned on D2l.

For part 2B-2 i am getting a large number but not the same as the one mentioned on D2l. But the number changes for various inputs

I was not sure what exactly to do, i refered to a lot of websites n hacking and understanding objdumps.
They were really confusing and confused me more.
Then i refered to the class slides and fllowed a similar approach.
Initially i was trying to debug to try and pinpoint the root cause.
Then i kind of figured out where the issue is, then i just messed around with the jump statements.
I wrote down all the jump and set statements and analyzed the program flow and just changed the conditions.

http://www.mathemainzel.info/files/x86asmref.html#setae
http://visualgdb.com/gdbreference/commands/delete
http://unixwiz.net/techtips/x86-jumps.html
https://sourceware.org/gdb/onlinedocs/gdb/Continuing-and-Stepping.html#Continuing-and-Stepping
http://www.mathemainzel.info/files/x86asmref.html#je
http://cs.baylor.edu/~donahoo/tools/gdb/tutorial.html
https://2600hertz.wordpress.com/2010/01/14/hacking-into-any-executable-using-gdb/
http://iosgods.com/topic/781-tutorial-how-to-hack-using-gnu-debugger-gdb/

COMMENTS:
It was really interesting, it would really help if you could give a few sample programs t hack / gain more knowledge.
