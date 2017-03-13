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

void replaceAll(std::string& str, const std::string& from, const std::string& to) {
  if(from.empty()) return;
  size_t start_pos = 0;
  while((start_pos = str.find(from, start_pos)) != std::string::npos) {
    str.replace(start_pos, from.length(), to);
    start_pos += to.length(); // In case 'to' contains 'from', like replacing 'x' with 'yx'
  }
}

int main() {
  unordered_map<string,int> authorToIndex;
  ifstream fin;
  fin.open("../data/outputacm2.txt");
  int n = 0;

  for(string line; getline(fin, line); ) {
    if(line[1]=='@') {
      line.erase(0,2);
      if(line != "") {
        replaceAll(line, ".,", ". ");
        replaceAll(line, ", ", " ");
        if(isInside(line,',')) {//if multiple authors
          stringstream ss(line);
          while(ss.good()) {
              string substr;
              getline( ss, substr, ',' );
              if(authorToIndex.find(substr) == authorToIndex.end()) {
                  authorToIndex[substr] = n ;
                  n++;
              }
          }
        }
        else if(authorToIndex.find(line)==authorToIndex.end()) {
          authorToIndex[line] = n;
          n++;
        }
      }
    }
  }
  fin.close();

  vector<vector<int> > authorToPapers(authorToIndex.size());

  ifstream fin2;
  fin2.open("../data/outputacm2.txt");
  ofstream myfile;
  myfile.open("../matrices/papersToAuthors.csv");
  vector<int> authorsPerPaper;
  int lineCount = 0;

  for(string line; getline(fin2, line); ) {
    if(line[1]=='@') {
      line.erase(0,2);
      replaceAll(line, ".,", ". ");
      replaceAll(line, ", ", " ");
      if(isInside(line,',')) { //multiply authors
        stringstream ss(line);
        while(ss.good()) {
          string substr;
          getline(ss, substr, ',');
          if(authorToIndex.find(substr) != authorToIndex.end()) {
            unordered_map<string,int>::iterator got = authorToIndex.find(substr);
            authorsPerPaper.push_back(got -> second);
            authorToPapers[got -> second].push_back(lineCount);
          }
        }
      }
      else if(line == "") {
        authorsPerPaper.push_back(-1);
      }
      else { //single author
        unordered_map<string,int>::iterator got = authorToIndex.find(line);
        authorsPerPaper.push_back(got -> second);
        authorToPapers[got -> second].push_back(lineCount);
      } //output the vector

      for(int i = 0; i <authorsPerPaper.size();i++) {
          if(i + 1 ==authorsPerPaper.size())
              myfile << authorsPerPaper[i];
          else
              myfile << authorsPerPaper[i]<< ",";
      }
      myfile << endl;
      authorsPerPaper.clear();
      lineCount++;
    }
  }

  ofstream authorToPapersOS;
  authorToPapersOS.open("../matrices/authorsToPapers.csv");
  for(int i=0; i<authorToPapers.size(); i++) {
    if(authorToPapers[i].size() != 0 ) {
      for(int j=0; j<authorToPapers[i].size(); j++) {
        if((j+1) == authorToPapers[i].size())
            authorToPapersOS << authorToPapers[i][j];
        else
            authorToPapersOS << authorToPapers[i][j] << ",";
      }
      authorToPapersOS << endl;
    }
  }
}
