import numpy as np

def BMA(patient,diseasesPhenos):

	bestScoresPatient=[]
	for phenoP in patient:
		i=order_dict[phenoP]
		# .strip() to remove the \n character if any
		#assume first is highest score then compare 
		j=order_dict[diseasesPhenos[0].strip()] 	
		maximum=simantic_similarity_matrix[i][j]
		for phenoD in diseasesPhenos[1:]:
			phenoD=phenoD.strip()
			j=order_dict[phenoD]
			if(simantic_similarity_matrix[i][j]>maximum):
				maximum=simantic_similarity_matrix[i][j]
		bestScoresPatient.append(maximum)
	
	bestScoresDisease=[]
	for phenoD in diseasesPhenos[]:
		phenoD=phenoD.strip()
		i=order_dict[phenoD]
		j=order_dict[patient[0]]#assume first is highest score then compare 
		maximum=simantic_similarity_matrix[i][j]
		for phenoP in patient[1:]:
			if(simantic_similarity_matrix[i][j]>maximum):
				maximum=simantic_similarity_matrix[i][j]
		bestScoresDisease.append(maximum)


	
	return (np.sum(bestScoresPatient)/len(bestScoresPatient)+np.sum(bestScoresDisease)/len(bestScoresDisease))/2



#loading the pre-computed scores of the phenotypes. 
resnik_similarity = np.loadtxt('simscores.txt', dtype = 'float64') #use this to load scores frpm .txt file 

#the following is to store the numpy array in a .npy file to use it instead of "simscores.txt"
#myfile = open("npArray.npy", "wb")
#np.save(myfile,resnik_similarity)
#myfile.close() 

# if you have the numpy array stored in .npy file 
#resnik_similarity= np.load("nparray.npy")


# 'HPandMP.txt' contains Phenotypes IDs 
phenotypes = np.genfromtxt('HPandMP.txt', dtype = 'str') 
simantic_similarity_matrix = resnik_similarity.reshape(len(phenotypes),len(phenotypes))

#to store the index of the phenotypes(from 'HPandMP.txt') in a dictionary in order to access the values in simantic_similarity_matrix 
order_dict = dict()
for i, pheno in enumerate(phenotypes):
	order_dict[pheno]=i

#pathoandpheno is a file that contains disases with their phenotypes tab seperated. 
# DOID_1 \t HP_1 HP_2 ... HP_N  

with open('pathoandpheno.txt') as f:
    lines = f.readlines()
    
#each element of the list  will contain a disease with its phenotypes
diseasesPhenos=[]
for line in lines:
	diseasesPhenos.append( line.split('\t') )




patient = ["HP_0002086","HP_0031504","HP_0000969"] #insert your patient phenotypes here

patientInDict=[]#to get only the phenotypes that we have in our dictionary (since we are getting the phenotypes from aber-owl API WE might get some phenotypes that we did not pre-compute their similarity with the other phenotypes)
for ph in patient:
	if(ph in order_dict):
		patientInDict.append(ph)

simScores=[]
for dis in diseasesPhenos 
	simScores.append(BMA(patientInDict,diseasesPhenos[1:]))# diseasesPhenos[1:] to send the phenotypes only without the disease ID
	
sort_similarity_arg = np.argsort(simScores)[::-1]#this contains the indexes of the diseases similaritty scores from highest yo lowest
bestMatchDisId=diseasesPhenos[sort_similarity_arg[0]][0]
print(bestMatchDisId)

#pathoandpheno is a file that contains disases with their phenotypes tab seperated. 
# DOID_1 \t NCBITaxon_1 NCBITaxon_2 ... NCBITaxon_N   
with open('dis-patho.txt') as f:
    lines = f.readlines()

pathos=[]
for line in lines: 
	dis = line.split('\t')[0]
	if(dis==bestMatchDisId):
		pathos=line.split('\t')

print(pathos)




