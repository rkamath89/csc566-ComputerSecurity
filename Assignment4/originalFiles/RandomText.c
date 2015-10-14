#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <openssl/evp.h>

/* To compile and run: 
 gcc -I/home/seed/openssl-1.0.1/include -o sample sample.c -L/home/seed/openssl-1.0.1/ -lcrypto -ldl 
 && ./sample md5
 */
char *rand_string(char *str, size_t size)
{
	const char charset[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	if (size) 
	{
		--size;
		size_t n;
		for (n = 0; n < size; n++) 
		{
			int key = rand() % (int)(sizeof charset - 1);
			str[n] = charset[key];
		}
		str[size] = '\0';
	}
	return str;
}
main(int argc, char *argv[])
{
    EVP_MD_CTX *mdctx;
    EVP_MD_CTX *mdctxdup;
    const EVP_MD *md;
    const EVP_MD *mddup;
    char mess1[] = "Hello World";
    unsigned char md_value[EVP_MAX_MD_SIZE];
    unsigned char md_valuedup[EVP_MAX_MD_SIZE];
    int md_len,md_lendup, i;
    char originalBinary[24];
    char randomBinary[24];
    OpenSSL_add_all_digests();
    
    if(!argv[1]) {
        printf("Usage: mdtest digestname\n");
        exit(1);
    }
    
    md = EVP_get_digestbyname(argv[1]) ;
    
    if(!md) {
        printf("Unknown message digest %s\n", argv[1]);
        exit(1);
    }
   
    mdctx = EVP_MD_CTX_create();
    EVP_DigestInit_ex(mdctx, md, NULL);
    EVP_DigestUpdate(mdctx, mess1, strlen(mess1));
    EVP_DigestFinal_ex(mdctx, md_value, &md_len);
    EVP_MD_CTX_destroy(mdctx);
    
    printf("Input Original: %s\n",mess1);
    printf("Digest Original: ");
    for(i = 0; i < md_len; i++)
    printf("%02x", md_value[i]);
    printf("\n");
    
int dontExit =1;
char *str ;
    
  while(dontExit)
   {
    str = (char*)malloc(10);
    str = rand_string(str,9);
    mdctxdup = EVP_MD_CTX_create();
    EVP_DigestInit_ex(mdctxdup, md, NULL);
    EVP_DigestUpdate(mdctxdup, str, strlen(str));
    EVP_DigestFinal_ex(mdctxdup, md_valuedup, &md_lendup);
EVP_MD_CTX_destroy(mdctxdup);
    

     if((md_value[0] == md_valuedup[0]) &&  (md_value[1] == md_valuedup[1])  && (md_value[2] == md_valuedup[2]) )
	{
        
	break;
	}
    free(str);
    }
    printf("Input Random: %s\n",str);
    printf("Digest Random: ");
    for(i = 0; i < md_lendup; i++)
    printf("%02x", md_valuedup[i]);
    printf("\n");

    
    /* Call this once before exit. */
    EVP_cleanup();
    exit(0);
}
