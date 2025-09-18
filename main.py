import requests
import datetime
import time

#TODO:
#написать функцию для получения обновляемого токена
#{MBKS4.3}
#сохранить файл, в котором был найден флаг. Записать когда он найден
#проверить если флаг уже существует, чтобы не тратить попытки отправки

class fileInfo:
    def __init__(self,fileLink,fileName,timeStamp,flag,contains):
        self.fileLink=fileLink
        self.fileName=fileName
        self.timeStamp=timeStamp
        self.flag=flag
        self.contains=contains

    def save(self):
        try:
            tempvar=self.fileName.split(".")
            textFile = open(f"{self.fileName}+{self.timeStamp}.{tempvar[len(tempvar)-1]}", "a")
            #{tempvar[len(tempvar-1)]} made to get exact file type(format)
            textFile.write("FLAG: "+self.flag+"\n\n\n"+self.contains)
            textFile.close()
        except Exception as e:
             print(e)

    def writeToLog(self):
        try:
            textFile = open(f"log.txt", "a")
            textFile.write(f"timestamp: {self.timeStamp}"+"\n")
            textFile.write(f"file name: {self.fileName}" + "\n")
            textFile.write(f"flag: {self.flag}" + "\n")
            textFile.write("\n")
            textFile.close()
        except Exception as e:
            print(e)
        return 0

    def send(self):
        requests.post(submitURL, data={
            "brigade": "4.3",
            "flag": self.flag
        })

    def printInfo(self):
        print(self.fileLink)
        print(self.fileName)
        print(self.timeStamp)
        print(self.flag)
        #print(self.contains)
        print(" ")
        return 0


def getToken(url):
    r = requests.get(url)
    token=r.text
    return token

def getFlag(link):
    flags = []
    r = requests.get(link)
    # print(r.text)
    filetext = (r.text).split("\n")
    for n in range(len(filetext)):
        if ("{MBKS4.3}" in filetext[n]):
            flags.append(filetext[n])
    #print(flags)
    return flags

def getContains(link):
    return ((requests.get(link)).text)

def getInfo(text, baseURL):
    files=[]
    text = text.split('\n')
    timeStamp = '{:%Y_%b_%d_%H_%M_%S_%f}'.format(datetime.datetime.now())
    for i in range(1,len(text)):
        if ("<td>" and "</td>" in text[i]) and ("." in text[i]) \
                and ("<a href=\"" in text[i]) and ("index.php" not in text[i]) and (".submit.php.swp" not in text[i]):
            link=baseURL + "/" + ((text[i].split("\""))[1])
            name=(text[i-1].split("<td>"))[1].split("</td>")[0]
            files.append(fileInfo(link,name,timeStamp,getFlag(link),getContains(link)))

    return files

def analyzeInfo(files):
    for i in range(len(files)):
        files[i].printInfo()
        if files[i].flag:
            files[i].writeToLog()
            files[i].save()
            try:
                f = open("flags.txt")
                flags = [str(i) for i in f]
                f.close()
                for n in range(len(flags)):
                    if files[i].flag in flags[n]:

                        raise Exception(f"Flag {files[i].flag} already exist!")
            except Exception as e:
                print(e)
            try:
                textFile = open(f"flags.txt", "a")
                textFile.write(files[i].flag)
                textFile.close()
                files[i].send()
            except Exception as e:
                print(e)

    return 0


baseURL="http://194.87.94.159/share"
tokenURL="http://194.87.94.159/share/token.php"
submitURL="http://194.87.94.159/share/submit.php"
period=25
t=0
while True:
    try:
        time.sleep(1)
        t += 1
        print(t)
        if t>=period:
            token=getToken(tokenURL)
            accessURL=f"http://194.87.94.159/share/?token={token}"
            print(token,accessURL)
            r=requests.get(accessURL)

            analyzeInfo(getInfo(r.text,baseURL))

            t=0
    except Exception as e:
        print(e)