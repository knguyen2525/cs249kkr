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
  unordered_map<string,int> venueToIndex;
  ifstream fin;
  fin.open("../data/outputacm2.txt");
  int n = 0;

  for(string line; getline(fin, line); ) {
    if(line[1]=='c') {
      line.erase(0,2);
      if(line != "") {
        if(isInside(line,',')) {//if multiple authors
          stringstream ss(line);
          while(ss.good()) {
              string substr;
              getline( ss, substr, ',' );
              if(venueToIndex.find(substr) == venueToIndex.end()) {
                  venueToIndex[substr] = n ;
                  n++;
              }
          }
        }
        else if(venueToIndex.find(line)==venueToIndex.end()) {
          venueToIndex[line] = n;
          n++;
        }
      }
    }
  }
  fin.close();

  vector<vector<int> > venueToPapers(venueToIndex.size());

  ifstream fin2;
  fin2.open("../data/outputacm2.txt");
  ofstream myfile;
  myfile.open("../matrices/papersToVenues.csv");
  vector<int> venuesPerPaper;
  int lineCount = 0;

  for(string line; getline(fin2, line); ) {
    if(line[1]=='c') {
      line.erase(0,2);
      if(isInside(line,',')) { //multiply authors
        stringstream ss(line);
        while(ss.good()) {
          string substr;
          getline(ss, substr, ',');
          if(venueToIndex.find(substr) != venueToIndex.end()) {
            unordered_map<string,int>::iterator got = venueToIndex.find(substr);
            venuesPerPaper.push_back(got -> second);
            venueToPapers[got -> second].push_back(lineCount);
          }
        }
      }
      else if(line == "") {
        venuesPerPaper.push_back(-1);
      }
      else { //single author
        unordered_map<string,int>::iterator got = venueToIndex.find(line);
        venuesPerPaper.push_back(got -> second);
        venueToPapers[got -> second].push_back(lineCount);
      } //output the vector

      for(int i = 0; i <venuesPerPaper.size();i++) {
          if(i + 1 ==venuesPerPaper.size())
              myfile << venuesPerPaper[i];
          else
              myfile << venuesPerPaper[i]<< ",";
      }
      myfile << endl;
      venuesPerPaper.clear();
      lineCount++;
    }
  }

  ofstream venueToPapersOS;
  venueToPapersOS.open("../matrices/venuesToPapers.csv");
  for(int i=0; i<venueToPapers.size(); i++) {
    if(venueToPapers[i].size() != 0 ) {
      for(int j=0; j<venueToPapers[i].size(); j++) {
        if((j+1) == venueToPapers[i].size())
            venueToPapersOS << venueToPapers[i][j];
        else
            venueToPapersOS << venueToPapers[i][j] << ",";
      }
      venueToPapersOS << endl;
    }
  }
}
