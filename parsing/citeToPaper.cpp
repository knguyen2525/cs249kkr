#include <iostream>
#include <string>
#include <fstream>
#include <algorithm>
#include <unordered_map>
#include <vector>
#include <sstream>
using namespace std;

int main()
{
    ifstream fin;
    fin.open("outputacm2.txt");

    ofstream myfile;
    myfile.open("data2.csv");

    vector<vector<int> > citesToPapersVector;
    unordered_map<string,int> citesToPapersHash;
    int paperCount = 0;
    bool inCites = 0;
    bool first = 1;
    vector<string>myvector;
    for(string line; getline(fin, line); )
    {
        if(line[1]=='%')
        {
            line.erase(0,2);
            myvector.push_back(line);
            inCites = 1;
            unordered_map<string,int>::iterator got = citesToPapersHash.find(line);
            if(got != citesToPapersHash.end())
            {
                int temp = got -> second;
                citesToPapersVector[temp].push_back(paperCount);
            }
            else
            {
                citesToPapersHash[line] = citesToPapersVector.size();
                vector<int> v;
                v.push_back(paperCount);
                citesToPapersVector.push_back(v);
            }
        }
        if(line[1] == '@' && inCites == 1)
        {
            paperCount++;
            for(int i = 0; i <myvector.size();i++)
            {
                cout << myvector[i]<< ",";
            }
            cout << endl;
            inCites = 0;
            myvector.clear();
        }
        else if(line[1] == '@' && inCites == 0)
        {
            if(first == 1)
                first = 0;
            else
            {
                paperCount++;
                cout << "-1," <<endl;
            }
        }
 


    }

        if(inCites == 0)
            cout << "-1," <<endl;
        else
        {
            for(int i = 0; i <myvector.size();i++)
            {
                cout << myvector[i]<< ",";
            }
        }
    cout << endl;
    for(int i = 0; i < citesToPapersVector.size();i++)
    {
        for(int j = 0; j < citesToPapersVector[i].size();j++)
        {
            cout << citesToPapersVector[i][j] << " ";
        }
        cout << endl;
    }
}


