
f=open("dis.txt","r")
fw = open("gold.txt", "w")

lines=f.readlines()
result=[]
i=0
fw.write("{")
for x in lines:
    x=x.rstrip("\n\r")
    if(i<7797):
        result.append("\""+x+"\""+": ["+"\""+"Fake-"+x+"\""+"], ")
    else:
        result.append("\""+x+"\""+": ["+"\""+x+"\""+"]")

    fw.write(result[i])
    i+=1
fw.write("}")
print(i)
fw.close()
f.close()
