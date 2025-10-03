import requests
import datetime
import json

# TODO:
# 1)+Сделать класс worker, содержащий информацию о имени пользователя, и словарь доступа к файлам вида {"+": [], "-":[]}
# 2)+Сделать парсер txt файла, где имя может содержать пробелы, имя файла не может. Каждый аргумент разделен пробелом
# 3)+Сделать функцию сравнения и выбора дальнейших действий:
#   сравнить с полученным списком:
#       +если нет указанного в списке пользователя или файла нет в матрице -> предложить выбор (добавить/игнор ?)
#       +если неверно указан доступ к существующим файлам -> исправить полученный список, записать в changelog исправления.
#           +Вернуть список после всех исправлений и вернуть содержимое changelog (каждый запуск - новый changelog)
# 4)+Написать функцию, которая печатает стандартную (json) ТАБЛИЦУ (матрицу) доступа. (такая же как и дается)
# 5)+Вывод списка активных доступов для конкретного указанного пользователя.
# 6)+Возможность выдачи прав доступа на заданный файл заданному пользователю в ручном режиме.
#
class worker:
    def __init__(self, name):
        self.name = name
        self.access = {"+": [], "-": []}

    def printInfo(self):
        print("User name:", self.name)
        print("Access:", self.access)
        print(" ")
        return 0


def getJSON(url):
    return json.loads(requests.get(url).text)


def writeToChangelog(str):
    timeStamp = '{:%Y_%b_%d %H_%M_%S_%f}'.format(datetime.datetime.now())
    try:
        f = open("log.txt", "a")
        f.write(timeStamp + "\n" + str + "\n\n")
        print(str + "\n")
        f.close()
        return 0
    except OSError as e:
        print(e)
        return 1

def showLog(path):
    try:
        f = open("log.txt", "r")
        print("----- LOGS -----")
        for i in f:
            print(str(i), end="")
        print("----- LOGS -----")
        f.close()
    except OSError as e:
        print(e)
    return


def send(user, file, value):
    submitURL = "http://194.87.94.159/supersec/api.php?action=set"
    try:
        requests.post(submitURL, data={
            "user": user,
            "file": file,
            "value": value
        })
    except Exception as e:
        print(e)
        return 1
    return 0


def readCorrectList(path):
    correctListWorkers = []
    correctList = []
    try:
        f = open(path, "r")
        correctList = [str(i) for i in f]
        f.close()
    except OSError as e:
        print(e)
        return 1
    if not correctList: return 2
    #print(correctList)

    for i in range(len(correctList)):
        userNames = []
        try:
            temp = correctList[i].split(" ")
            # print(temp)
            value = ((temp[(len(temp) - 1) - 0]).split("\n"))[0]
            file = temp[(len(temp) - 1) - 1]
            name = ""
            for n in range(len(temp) - 2):
                if n == (len(temp) - 2 - 1):
                    name += temp[n]
                else:
                    name += (temp[n] + " ")
        except Exception as e:
            print(e)
            continue

        if name not in userNames:
            userNames.append(name)
            tempObj = worker(name)
            if value == "+":
                tempObj.access["+"].append(file)
            else:
                tempObj.access["-"].append(file)
            correctListWorkers.append(tempObj)
        else:
            for m in range(len(correctListWorkers)):
                if correctListWorkers[m] == name:
                    if value == "+":
                        correctListWorkers[m].access["+"].append(file)
                    else:
                        correctListWorkers[m].access["-"].append(file)

    return correctListWorkers


def readJSONList(data):
    JSONWorkers = []
    userNames = []

    for file in data:
        for name in data[file]:
            if name not in userNames:
                userNames.append(name)
                tempObj = worker(name)
                if data[file][name] == "+":
                    (tempObj.access)["+"].append(file)
                else:
                    (tempObj.access)["-"].append(file)
                JSONWorkers.append(tempObj)
            else:
                for i in range(len(JSONWorkers)):
                    if JSONWorkers[i].name == name:
                        if data[file][name] == "+":
                            (JSONWorkers[i].access)["+"].append(file)
                        else:
                            (JSONWorkers[i].access)["-"].append(file)
    return JSONWorkers


def compareWorkers(JSONWorkers, correctListWorkers):
    try:
        f = open("log.txt", "w")
        f.close()
    except OSError as e:
        print(e)
    for i in range(len(correctListWorkers)):
        for n in range(len(JSONWorkers)):
            # print(f"COMPARING {correctListWorkers[i].name} WITH {JSONWorkers[n].name}")
            if correctListWorkers[i].name == JSONWorkers[n].name:
                # print(correctListWorkers[i].name)
                correct = False
                incorrect = False
                noRecord = False
                if len(correctListWorkers[i].access["+"]) > len(correctListWorkers[i].access["-"]):
                    correctValue = "+"
                    for m in range(len(JSONWorkers[n].access["+"])):
                        if (correctListWorkers[i].access["+"][0]) == (JSONWorkers[n].access["+"][m]):
                            correct = True
                    for m in range(len(JSONWorkers[n].access["-"])):
                        if (correctListWorkers[i].access["+"][0]) == (JSONWorkers[n].access["-"][m]):
                            incorrect = True
                    if not (correct or incorrect): noRecord = True
                else:
                    correctValue = "-"
                    for m in range(len(JSONWorkers[n].access["-"])):
                        if (correctListWorkers[i].access["-"][0]) == (JSONWorkers[n].access["-"][m]):
                            correct = True
                    for m in range(len(JSONWorkers[n].access["+"])):
                        if (correctListWorkers[i].access["-"][0]) == (JSONWorkers[n].access["+"][m]):
                            incorrect = True
                    if not (correct or incorrect): noRecord = True
                # print(f"CORRECT {correct}; INCORRECT {incorrect}; NO RECORD {noRecord}")

                if incorrect:
                    file = correctListWorkers[i].access[correctValue][0]
                    if correctValue == "+":
                        incorrectValue = "-"
                    else:
                        incorrectValue = "+"
                    # print(correctValue, incorrectValue)
                    for m in range(len(JSONWorkers[n].access[incorrectValue])):
                        if JSONWorkers[n].access[incorrectValue][m] == file:
                            JSONWorkers[n].access[incorrectValue].pop(m)
                            break
                    (JSONWorkers[n].access[correctValue]).append(file)
                    writeToChangelog(f"Name: {JSONWorkers[n].name}; File: {file}; "
                                     f"From {incorrectValue} to {correctValue}")
                    send(JSONWorkers[n].name,file,correctValue)

                if noRecord:
                    file = correctListWorkers[i].access[correctValue][0]
                    (JSONWorkers[n].access[correctValue]).append(file)
                    writeToChangelog(f"Name: {JSONWorkers[n].name}; File: {file}; Added to {correctValue}")
                    send(JSONWorkers[n].name,file,correctValue)

    JSONNames = []
    for i in range(len(JSONWorkers)): JSONNames.append(JSONWorkers[i].name)

    for i in range(len(correctListWorkers)):
        name = correctListWorkers[i].name
        if len(correctListWorkers[i].access["+"]) > len(correctListWorkers[i].access["-"]):
            correctValue = "+"
        else:
            correctValue = "-"
        file = correctListWorkers[i].access[correctValue][0]
        if name not in JSONNames:
            print(f"There is no user with name \"{name}\" in matrix!")
            print("Choose action: (write a number)\n1) Add user \n2) Ignore")
            try:
                action = int(input())
            except Exception as e:
                print("Error:", e)
                action = 2
            if action == 1:
                JSONWorkers.append(worker(name))
                JSONWorkers[len(JSONWorkers) - 1].access[correctValue].append(file)
                writeToChangelog(f"User added:\nName: {name}; File: {file}; Value: {correctValue}")
                send(name,file,correctValue)
            else:
                print("Ignored.")

    print("All changes are written to log.txt. Would you like to see it?\n1) Yes\n2) No")
    try:
        action = int(input())
    except Exception as e:
        print("Error:", e)
        action = 2
    if action == 1:
        showLog("log.txt")
    else:
        print("Ignored.")
    input("Press \"Enter\" to continue...")
    return JSONWorkers


def printMatrix(matrix):
    files = {}
    for i in range(len(matrix)):
        for n in range(len(matrix[i].access["+"])):
            files[(matrix[i].access["+"])[n]] = {}
        for n in range(len(matrix[i].access["-"])):
            files[(matrix[i].access["-"])[n]] = {}

    for i in range(len(matrix)):
        for n in range(len(matrix[i].access["+"])):
            files[(matrix[i].access["+"])[n]].update({matrix[i].name: "+"})
        for n in range(len(matrix[i].access["-"])):
            files[(matrix[i].access["-"])[n]].update({matrix[i].name: "-"})
    #print(files)
    return json.dumps(files, indent="\t")


def userAccesses(objArray):
    print("\nChoose which user accesses you want to see:")
    input("Press \"Enter\" to continue...")
    for i in range(len(objArray)):
        print(f"{i + 1}) {objArray[i].name}")
    try:
        action = int(input()) - 1
        print("Name:", objArray[action].name)
        return json.dumps(objArray[action].access, indent="\t")
    except Exception as e:
        print("Error:", e)
        action = 0
    if action == 0: return ("Ignored.")


def changeUserAccess(objArray):
    files = []
    file=""
    user=""
    for i in range(len(objArray)):
        for n in range(len(objArray[i].access["+"])):
            files.append(objArray[i].access["+"][n])
        for n in range(len(objArray[i].access["-"])):
            files.append(objArray[i].access["-"][n])
    files = list(set(files))
    # print(files)
    print("\nChoose which file you want to change access to:")
    input("Press \"Enter\" to continue...")
    for i in range(len(files)):
        print(f"{i + 1}) {files[i]}")
    try:
        action = int(input()) - 1
        file=files[action]
        print(f"File {file} have been chosen")
    except Exception as e:
        print("Error:", e)
        action = -1
    if action == -1: return("Ignored.")

    print("\nChoose which user access you want to change:")
    input("Press \"Enter\" to continue...")
    for i in range(len(objArray)):
        print(f"{i + 1}) {objArray[i].name}")
    try:
        action = int(input()) - 1
        user=objArray[action].name
        print(f"User {user} have been chosen")
    except Exception as e:
        print("Error:", e)
        action = -1
    if action == -1: return("Ignored.")

    currentValue=""
    index=-1
    for i in range(len(objArray)):
        if objArray[i].name==user:
            index=i
            for n in range(len(objArray[i].access["+"])):
                if objArray[i].access["+"][n]==file:
                    currentValue="+"
            for n in range(len(objArray[i].access["-"])):
                if objArray[i].access["-"][n]==file:
                    currentValue="-"
    if index==-1: return("Error")
    if currentValue=="":
        print(f"User {user} does not have any access to file {file}")
        print("\nChoose an option:\n1)Give access (\"+\") \n2)Remove access (\"-\")")
        try:
            action = int(input())
            if action==1:
                objArray[index].access["+"].append(file)
                send(user,file,"+")
                writeToChangelog(f"User: {user}; File: {file}; Now have \"+\" access")
            elif action==2:
                objArray[index].access["-"].append(file)
                send(user, file, "-")
                writeToChangelog(f"User: {user}; File: {file}; Now have \"-\" access")
            else:
                raise Exception("Incorrect number given")

        except Exception as e:
            print("Error:", e)
            action = 0
        if action == 0: return ("Ignored.")
    else:
        newValue= ""
        print(f"User {user} have access \"{currentValue}\" to file {file}")
        print("\nChoose an option:\n1)Give access (\"+\") \n2)Remove access (\"-\")")
        try:
            action = int(input())
            if action == 1:
                newValue = "+"
                if currentValue=="+":
                    raise Exception("Already given.")
                objArray[index].access["+"].append(file)
                for i in range(len(objArray[index].access["-"])):
                    if objArray[index].access["-"][i]==file:
                        objArray[index].access["-"].pop(i)
                        break
            elif action == 2:
                newValue = "-"
                if currentValue == "-":
                    raise Exception("Already given.")
                (objArray[index].access["-"]).append(file)
                for i in range(len(objArray[index].access["+"])):
                    if (objArray[index].access["+"])[i] == file:
                        objArray[index].access["+"].pop(i)
                        break
            else:
                raise Exception("Incorrect number given")
        except Exception as e:
            print("Error:", e)
            action = 0
        if action == 0: return ("Ignored.")
        send(user, file, newValue)
        writeToChangelog(f"User: {user}; File: {file}; Now have \"{newValue}\" access")
    return

matrix=[]
correctList=[]

matrixURL = "http://194.87.94.159/supersec/api.php?action=get"
# print(getJSON(matrixURL))
data = getJSON(matrixURL)
matrix = readJSONList(data)
correctList = readCorrectList("test.txt")
matrix = compareWorkers(matrix, correctList)

while True:
    print("Choose action (1...4):")
    print("1) Show full matrix \n2) Show user accesses \n3) Change user-file access (manual) \n4) Show log \n0) Exit")
    try:
        action = int(input())
        if action==1:
            print(printMatrix(matrix))
            print(requests.get(matrixURL).text)
        elif action==2:
            print(userAccesses(matrix))
        elif action==3:
            changeUserAccess(matrix)
        elif action==4:
            showLog("log.txt")
        elif action==0:
            break
        else: raise Exception("Incorrect number given")
        input("Press \"Enter\" to continue...")
    except Exception as e:
        print("Error:", e)
        action = -1
    if action == -1:
        print("Ignored.")
        continue
