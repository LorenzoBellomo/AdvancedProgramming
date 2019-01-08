def ciaoWord(s): 
    ciaoWord = ''.join(sorted(s)).lower()
    return ciaoWord

def addCiaoDict(ciaoDict, word):
    word = ''.join('' if c in '\n\t' else c for c in word).strip()
    ciaod = ciaoWord(word)
    if ciaod not in ciaoDict:
        ciaoDict[ciaod] = set()
    ciaoDict[ciaod].add(word)
    
def createDict(path):
    dict = {}
    with open(path, 'r') as file:
        for line in file:
            addCiaoDict(dict, line)
    return dict

def replaceAnagram(dict, line):
    ciaod = ciaoWord(line)
    if ciaod in dict:
        a = set()
        a.add(line)
        diff = dict[ciaod] - a
        if len(diff) != 0:
            line = diff.pop()
    return line



dict = createDict('auxiliaryfiles/anagram.txt')
print(dict)
print(replaceAnagram(dict, 'worbuierbaouabr'))
print(replaceAnagram(dict, 'skate'))

