
#include <math.h>

extern int fprintf(struct t___0 *stream , char const   *format  , ...) ;
void secret1(unsigned long num1 , unsigned long *num2 ) ;
extern long strtol(char const   *str , char const   *endptr , int base ) ;
int main(int argc , char *argv[] ) ;

void secret1(unsigned long num1 , unsigned long *num2 )
{
	unsigned long arrayNum1[2] ;
	unsigned long i ;
	char val ;
	unsigned long lshift ;

	arrayNum1[0UL] = num1 & 67929800UL;
	unsigned long value =  arrayNum1[0] >> 8UL;
	lshift = ( value & 7UL) | 1UL;
	arrayNum1[1UL] = num1 << lshift;

	for(i = 0; i < 2 ;i++)
	{
		val = *((char *)(& arrayNum1[i]) + 5);
		*((char *)(& arrayNum1[i]) + 5) = *((char *)(& arrayNum1[i]) + 0);
		*((char *)(& arrayNum1[i]) + 0) = val;
	}

	unsigned long power= ((arrayNum1[1] >> 1UL) & 15UL) | 1UL;
	unsigned long rslt = arrayNum1[0] >> power;
	unsigned long value1 = (((arrayNum1[1] >> 1UL ) & 15UL) | 1);
	unsigned long sub = 64 -value1;
	unsigned long power1 = arrayNum1[0] << sub;
	unsigned long nnn = 18446744073352546735;
	*num2 = ( rslt | power1 ) | nnn;
}
int main(int argc , char *argv[])
{
  unsigned long number1 ;
  unsigned long number2;

  if(argc < 2)
  {
	  printf("\n Call this program with 1 arguments\n");
	  return (0);
  }

  number1 = strtoul(argv[1], 0,10);
  secret1(number1, &number2);
  printf("%lu\n", number2);
  return (0);
}


