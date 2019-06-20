
f=open("dis.txt","r")
fw = open("disnames.txt", "w")

lines=f.readlines()
result=[]
i=0
for x in lines:
    result.append(x.split('\t')[0])
    fw.write(result[i].replace('_',':')+"\n")
    i+=1
fw.close()
f.close()
