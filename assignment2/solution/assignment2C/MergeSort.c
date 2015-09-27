
#include<stdio.h>
#include<stdlib.h>

void MergeSort(int *numArr,int n); 
void Merge(int *numArr,int *leftArray,int leftCount,int *rightArray,int rightCount);
void MergeSort(int *numArr,int n) 
{
	int mid,i, *leftArray, *rightArray;
	if(n < 2) 
		return;

	mid = n/2;

	leftArray = (int*)malloc(mid*sizeof(int)); 
	rightArray = (int*)malloc((n- mid)*sizeof(int)); 
	
	for(i = 0;i<mid;i++) 
	{
		leftArray[i] = numArr[i]; 
	}
	for(i = mid;i<n;i++) 
	{
		rightArray[i-mid] = numArr[i]; 
	}

	MergeSort(leftArray,mid);
	MergeSort(rightArray,n-mid);
	Merge(numArr,leftArray,mid,rightArray,n-mid);
   free(leftArray);free(rightArray);
}

void Merge(int *numArr,int *leftArray,int leftCount,int *rightArray,int rightCount) {
	int i,j,k;
	i = 0; j = 0; k =0;

	while(i<leftCount && j< rightCount) 
	{
		if(leftArray[i]  < rightArray[j]) numArr[k++] = leftArray[i++];
		else numArr[k++] = rightArray[j++];
	}
	while(i < leftCount) numArr[k++] = leftArray[i++];
	while(j < rightCount) numArr[k++] = rightArray[j++];
}


int main() 
{
	
	int i,numElem;
	int numArr[] = {3,11,14,1,0,2,8,6,5};
	

	numElem = sizeof(numArr)/sizeof(numArr[0]); 
	MergeSort(numArr,numElem);
	/*for(i = 0;i < numElem;i++) 
	{
			printf("%d",numArr[i]);
	}
	printf("\n");*/
	return 0;
}