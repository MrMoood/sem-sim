import numpy as np

def BMA(patient,dis):

	bestScoresP=[]
	for phenoP in patient:
		i=order_dict[phenoP]
		j=order_dict[dis[1].strip()]
		maximum=sim_mat[i][j]
		for phenoD in dis[1:]:
			phenoD=phenoD.strip()
			j=order_dict[phenoD]
			if(sim_mat[i][j]>maximum):
				maximum=sim_mat[i][j]
		bestScoresP.append(maximum)
	
	bestScoresD=[]
	for phenoD in dis[1:]:
		phenoD=phenoD.strip()
		i=order_dict[phenoD]
		j=order_dict[patient[0]]
		maximum=sim_mat[i][j]
		for phenoP in patient[1:]:
			if(sim_mat[i][j]>maximum):
				maximum=sim_mat[i][j]
		bestScoresD.append(maximum)


	
	return (np.sum(bestScoresP)/len(bestScoresP)+np.sum(bestScoresD)/len(bestScoresD))/2



#loading the pre-computed scores of the phenotypes. (float128 to make it more precise )
res_sim = np.loadtxt('simscores.txt', dtype = 'float64')
myfile = open("npArray2.txt", "wb")
np.save(myfile,res_sim)
myfile.close() 



phenotypes = np.genfromtxt('HPandMP.txt', dtype = 'str')
sim_mat = res_sim.reshape(len(phenotypes),len(phenotypes))


order_dict = dict()
for i, pheno in enumerate(phenotypes):
	order_dict[pheno]=i


with open('pathoandpheno.txt') as f:
    lines = f.readlines()
diseases=[]
for line in lines:
	diseases.append( line.split('\t') )




patient = ["HP_0002086","HP_0031504","HP_0000969"]
patientInDict=[]#to get only the phenotypes that we have in our dictionary 

for ph in patient:
	if(ph in order_dict):
		patientInDict.append(ph)

			

simScores=[]
for dis in diseases:
	simScores.append(BMA(patient,dis))
sort_similarity_arg = np.argsort(simScores)[::-1]

bestMatchDisName=diseases[sort_similarity_arg[0]][0]
print(bestMatchDisName)

with open('dis-patho.txt') as f:
    lines = f.readlines()

pathos=[]
for line in lines: 
	dis = line.split('\t')[0]
	if(dis==bestMatchDisName):
		pathos=line.split('\t')

print(pathos)



