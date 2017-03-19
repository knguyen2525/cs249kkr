#include <iostream>
#include <string>
#include <fstream>
#include <algorithm>
#include <unordered_map>
#include <vector>
#include <sstream>
using namespace std;

bool isInside(const string & str, char c) {
    return str.find(c) != string::npos;
}

int main() {
  unordered_map<string,int> authorToIndex;
  ifstream fin;
  fin.open("../matrices/papersToVenues.csv");
  int count = 0;

  for(string line; getline(fin, line); ) {
      if(line != "-1") {
        if(isInside(line,',')) {//if multiple authors
          stringstream ss(line);
          while(ss.good()) {
              string substr;
              getline( ss, substr, ',' );
              count++;
          }
        }
        else {
          count++;
        }
      }
    }
  fin.close();
  cout << count << endl;
}
