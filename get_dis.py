import pdb
import numpy as np
from sklearn.metrics import auc
import json


diseases = np.genfromtxt('disnames.txt', dtype = 'str')
sim_scores = np.loadtxt('Resnik.genxdisy.txt', dtype = 'float32')


sim_dict = dict()
for i,dis in enumerate(diseases):
	sim_dict[dis] = sim_scores[i]


sort_similarity_arg = np.argsort(sim_scores)[::-1]

sort_dis = [diseases[arg] for arg in sort_similarity_arg]


print('Number of Disease: {}'.format(len(diseases)))


with open('rankOfDis.txt', 'w') as file:
	for i, dis in enumerate(sort_dis):
		file.write('{}-{} sim={}\n'.format((i+1),dis,sim_dict[dis]))



corresponding_dis=sort_dis[0].replace(':','_')
print(corresponding_dis)

f=open("dis-patho.txt","r")
lines=f.readlines()
f.close()
for line in lines:
	if(line.split('\t')[0]==corresponding_dis):
		pathogens=line.split('\t')

with open('pathogens.txt', 'w') as file:
	file.write(' the disease is {} and the pathogenes are:\n'.format(corresponding_dis))
	for i in range (1, len(pathogens)):
		file.write('{}\t'.format(pathogens[i]))

#print(lines[74].split('\t')[1])


#pdb.set_trace()
