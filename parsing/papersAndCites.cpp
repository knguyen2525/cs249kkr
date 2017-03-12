#include <iostream>
#include <string>
#include <fstream>
#include <algorithm>
#include <unordered_map>
#include <vector>
#include <sstream>
using namespace std;

int main() {
    ifstream fin;
    fin.open("../data/outputacm2.txt");

    ofstream os;
    os.open("../matrices/paperToCites.csv");

    ofstream os1;
    os1.open("../matrices/citeToPapers.csv");

    vector<vector<int> > citesToPapersVector;
    unordered_map<string,int> citesToPapersHash;
    int paperCount = 0;
    bool inCites = 0;
    bool first = 1;
    vector<string> myvector;
    for(string line; getline(fin, line);) {
      if(line[1]=='%') {
        line.erase(0,2);
        myvector.push_back(line);
        inCites = 1;
        unordered_map<string,int>::iterator got = citesToPapersHash.find(line);
        if(got != citesToPapersHash.end()) {
            int temp = got -> second;
            citesToPapersVector[temp].push_back(paperCount);
        }
        else {
            citesToPapersHash[line] = citesToPapersVector.size();
            vector<int> v;
            v.push_back(paperCount);
            citesToPapersVector.push_back(v);
        }
      }
      if(line[1] == '@' && inCites == 1) {
        paperCount++;
        for(int i=0; i<myvector.size(); i++) {
          if(i+1 == myvector.size())
            os << myvector[i];
          else
            os << myvector[i] << ",";
        }

        os << endl;
        inCites = 0;
        myvector.clear();
      }
      else if(line[1] == '@' && inCites == 0) {
        if(first == 1)
          first = 0;
        else {
          paperCount++;
          os << "-1" << endl;
        }
      }
    }
    if(inCites == 0)
      os << "-1" << endl;
    else
      for(int i=0; i<myvector.size(); i++) {
        if(i+1 == myvector.size())
          os << myvector[i];
        else
          os << myvector[i] << ",";
      }

    for(int i=0; i<citesToPapersVector.size(); i++) {
      for(int j=0; j<citesToPapersVector[i].size(); j++) {
        if(j+1 == citesToPapersVector[i].size())
          os1 << citesToPapersVector[i][j];
        else
          os1 << citesToPapersVector[i][j] << ",";
      }
      os1 << endl;
    }
}
